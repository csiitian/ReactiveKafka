package org.example.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsOptions;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.example.properties.KafkaProducerProperties;
import org.example.properties.KafkaTopicProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaAdminConfig {

    private final KafkaTopicProperties kafkaTopicProperties;
    private final KafkaProducerProperties kafkaProducerProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getBootstrapServers());

        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createTopic() {
        return TopicBuilder.name(kafkaTopicProperties.getName())
                .partitions(kafkaTopicProperties.getNumPartitions())
                .replicas(kafkaTopicProperties.getReplicationFactors())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "snappy")
                .build();
    }
}
