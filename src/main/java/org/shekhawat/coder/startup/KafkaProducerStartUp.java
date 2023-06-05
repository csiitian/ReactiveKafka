package org.shekhawat.coder.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shekhawat.coder.kafka.KafkaProducer;
import org.shekhawat.coder.redis.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@Order(0)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "startup.producer", value = "enabled", havingValue = "true")
public class KafkaProducerStartUp implements ApplicationListener<ApplicationReadyEvent> {

    private final KafkaProducer kafkaProducer;
    private final RedisService redisService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info(">> Application Event: >> Producer Started.");
        for(int i=0;i<10000;i++) {
            String key = "V-" + i;
            String value = "V-" + i;
            kafkaProducer.send(key, value)
                    .doOnNext(stringSenderResult -> {
                        redisService.storeData(key, value, Duration.ofDays(1));
                    })
                    .log()
                    .subscribe();
        }
    }
}
