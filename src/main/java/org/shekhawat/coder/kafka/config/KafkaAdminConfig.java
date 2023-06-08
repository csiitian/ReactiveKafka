package org.shekhawat.coder.kafka.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.shekhawat.coder.kafka.properties.KafkaProducerProperties;
import org.shekhawat.coder.kafka.properties.KafkaProperties;
import org.shekhawat.coder.kafka.properties.KafkaTopicProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaAdminConfig {

    private final KafkaProducerProperties kafkaProducerProperties;

    @Bean
    public KafkaAdmin kafkaAdmin(KafkaProperties kafkaProperties) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createTopic(KafkaTopicProperties kafkaTopicProperties) {
        return TopicBuilder.name(kafkaTopicProperties.getName())
                .partitions(kafkaTopicProperties.getNumPartitions())
                .replicas(kafkaTopicProperties.getReplicationFactors())
                .config(TopicConfig.COMPRESSION_TYPE_CONFIG, "snappy")
                .build();
    }
}
