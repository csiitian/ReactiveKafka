package org.shekhawat.coder.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shekhawat.coder.kafka.KafkaProducer;
import org.shekhawat.coder.redis.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "startup.producer", value = "enabled", havingValue = "true")
public class KafkaProducerStartUp extends AbstractStartUpApplication {

    private final KafkaProducer kafkaProducer;
    private final RedisService redisService;

    @Override
    public void startUp() {
        log.info(">> Application Event: >> Producer Started.");
        for(int i=10000;i<100000;i++) {
            String key = "V-" + i;
            String value = "V-" + i;
            kafkaProducer.send(key, value)
                    .doOnNext(stringSenderResult -> {
                        redisService.storeData(key, value, Duration.ofDays(1))
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe();
                    })
                    .log()
                    .subscribe();
        }
    }
}
