package org.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("spring.kafka.topic")
public class KafkaTopicProperties {

    private String name;
    private int numPartitions;
    private short replicationFactors;
}
