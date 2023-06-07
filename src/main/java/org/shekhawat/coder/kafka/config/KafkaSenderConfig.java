package org.shekhawat.coder.kafka.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.shekhawat.coder.kafka.properties.KafkaProducerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaSenderConfig {

    @Bean("kafkaSender")
    public <K, V> KafkaSender<K, V> kafkaSender(KafkaProducerProperties kafkaProducerProperties) {
        Map<String, Object> producerPros = new HashMap<>();
        producerPros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());
        producerPros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getKeySerializer());
        producerPros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerProperties.getValueSerializer());
        producerPros.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerProperties.getLingeringMs());
        producerPros.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerProperties.getCompressionType());
        producerPros.put(ProducerConfig.ACKS_CONFIG, kafkaProducerProperties.getAcks());

        return KafkaSender.create(SenderOptions.create(producerPros));
    }
}
