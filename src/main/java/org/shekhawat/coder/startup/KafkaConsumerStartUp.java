package org.shekhawat.coder.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shekhawat.coder.kafka.KafkaConsumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(prefix = "startup.consumer", value = "enabled", havingValue = "true")
public class KafkaConsumerStartUp extends AbstractStartUpApplication {

    private final KafkaConsumer kafkaConsumer;

    @Override
    public void startUp() {
        log.info(">> Application Event: >> Consumer Started.");
        kafkaConsumer.start();
    }
}
