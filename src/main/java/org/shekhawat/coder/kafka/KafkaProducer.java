package org.shekhawat.coder.kafka;

import lombok.extern.slf4j.Slf4j;
import org.shekhawat.coder.kafka.properties.KafkaTopicProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.kafka.sender.KafkaSender;

@Slf4j
@Service
public class KafkaProducer extends AbstractProducer<String, String, String> {

    public KafkaProducer(@Qualifier("kafkaSender") KafkaSender<String, String> kafkaSender,
                         KafkaTopicProperties kafkaTopicProperties) {
        super(kafkaSender, kafkaTopicProperties);
    }
}
