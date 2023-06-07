package org.shekhawat.coder.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shekhawat.coder.kafka.KafkaFilterConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "startup.filter.consumer", value = "enabled", havingValue = "true")
public class KafkaFilterConsumerStartUp extends AbstractStartUpApplication {

    private final KafkaFilterConsumer kafkaFilterConsumer;

    @Override
    public void startUp() {
        log.info(">> Application Event: >> Filter Consumer Started.");
        kafkaFilterConsumer.start();
    }
}
