package org.shekhawat.coder.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("spring.kafka.producer")
public class KafkaProducerProperties {

    private String bootstrapServers;
    private String keySerializer;
    private String valueSerializer;
    private Long lingeringMs;
    private String compressionType;
    private String acks;
}
