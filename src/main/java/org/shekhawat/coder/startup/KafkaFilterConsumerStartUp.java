package org.shekhawat.coder.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shekhawat.coder.kafka.KafkaConsumer;
import org.shekhawat.coder.kafka.KafkaFilterConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Order(0)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "startup.filter.consumer", value = "enabled", havingValue = "true")
public class KafkaFilterConsumerStartUp implements ApplicationListener<ApplicationReadyEvent> {

    private final KafkaFilterConsumer kafkaFilterConsumer;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info(">> Application Event: >> Filter Consumer Started.");
        kafkaFilterConsumer.start();
    }
}
