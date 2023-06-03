package org.example.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kafka.KafkaConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Order(0)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "startup.consumer", value = "enabled", havingValue = "true")
public class KafkaConsumerStartUp implements ApplicationListener<ApplicationReadyEvent> {

    private final KafkaConsumer kafkaConsumer;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Consumer Started.");
        kafkaConsumer.startConsumer();
    }
}
