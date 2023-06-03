package org.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.dto.Result;
import org.example.properties.KafkaTopicProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Service
public class KafkaProducer {

    private final KafkaSender<String, String> kafkaSender;
    private final KafkaTopicProperties kafkaTopicProperties;

    public KafkaProducer(@Qualifier("kafkaSender") KafkaSender<String, String> kafkaSender,
                         KafkaTopicProperties kafkaTopicProperties) {
        this.kafkaSender = kafkaSender;
        this.kafkaTopicProperties = kafkaTopicProperties;
    }

    public Mono<Result> sendRecord(String key, String value) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(
                kafkaTopicProperties.getName(), key, value);
        SenderRecord<String, String, String> senderRecord = SenderRecord
                .create(producerRecord, null);
        return kafkaSender.send(Mono.just(senderRecord))
                .log("Producer Send")
                .flatMap(record -> {
                    if(record.exception() != null) {
                        log.error("Error while sending the event to kafka.");
                        return Mono.just(Result.FAILURE);
                    } else {
                        log.info("Record Published.");
                        return Mono.just(Result.SUCCESS);
                    }
                })
                .last();
    }
}
