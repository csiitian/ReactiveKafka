package org.shekhawat.coder.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.shekhawat.coder.kafka.properties.KafkaConsumerProperties;
import org.shekhawat.coder.kafka.properties.KafkaProperties;
import org.shekhawat.coder.kafka.properties.KafkaTopicProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverPartition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaReceiverConfig {

    @Bean("kafkaReceiver")
    public <K, V> KafkaReceiver<K, V> kafkaReceiver(KafkaTopicProperties kafkaTopicProperties,
                                                    KafkaProperties kafkaProperties,
                                                    KafkaConsumerProperties kafkaConsumerProperties) {

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroupId());
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerProperties.getKeyDeserializer());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerProperties.getValueDeserializer());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerProperties.getAutoOffsetReset());

        ReceiverOptions<K, V> receiverOptions = ReceiverOptions
                .<K, V>create(consumerProps)
                .addAssignListener(receiverPartitions -> receiverPartitions.forEach(ReceiverPartition::seekToBeginning))
                .subscription(Collections.singletonList(kafkaTopicProperties.getName()));

        return KafkaReceiver.create(receiverOptions);
    }
}
