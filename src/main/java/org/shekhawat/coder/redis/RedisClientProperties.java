package org.shekhawat.coder.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("redis.client")
public class RedisClientProperties {

    private String hostname;
    private int port;
    private String prefix;
}
