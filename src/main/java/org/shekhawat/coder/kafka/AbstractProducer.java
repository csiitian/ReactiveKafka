package org.shekhawat.coder.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.shekhawat.coder.kafka.properties.KafkaTopicProperties;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

public abstract class AbstractProducer<K, V, T> {

    protected final KafkaSender<K, V> kafkaSender;
    protected final KafkaTopicProperties kafkaTopicProperties;

    protected AbstractProducer(KafkaSender<K, V> kafkaSender, KafkaTopicProperties kafkaTopicProperties) {
        this.kafkaSender = kafkaSender;
        this.kafkaTopicProperties = kafkaTopicProperties;
    }

    public Mono<SenderResult<T>> send(K key, V value, T correlationMetadata) {
        ProducerRecord<K, V> producerRecord = new ProducerRecord<>(
                kafkaTopicProperties.getName(), key, value);
        SenderRecord<K, V, T> senderRecord = SenderRecord
                .create(producerRecord, correlationMetadata);
        return kafkaSender.send(Mono.just(senderRecord))
                .last();
    }

    public Mono<SenderResult<T>> send(K key, V value) {
        return send(key, value, null);
    }
}