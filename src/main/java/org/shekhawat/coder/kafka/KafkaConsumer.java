package org.shekhawat.coder.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;

@Slf4j
@Service
@EnableScheduling
public class KafkaConsumer extends AbstractConsumer<String, String> {

    public KafkaConsumer(@Qualifier("kafkaReceiver") KafkaReceiver<String, String> kafkaReceiver) {
        super(kafkaReceiver, 1, Duration.ofSeconds(1));
    }

    @Override
    void process(ReceiverRecord<String, String> stringStringReceiverRecord) {
        log.info("Item Processed for {}", stringStringReceiverRecord.timestamp());
    }
}