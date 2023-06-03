package org.example.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.KafkaProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(0)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "startup.producer", value = "enabled", havingValue = "true")
public class KafkaProducerStartUp implements ApplicationListener<ApplicationReadyEvent> {

    private final KafkaProducer kafkaProducer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Producer Started.");
        for(int i=0;i<10000;i++) {
            String key = "V";
            String value = "V-" + i;
            kafkaProducer.sendRecord(key, value)
                    .subscribe();
        }
    }
}
