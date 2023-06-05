package org.shekhawat.coder.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Service
@EnableScheduling
public class KafkaConsumer extends AbstractConsumer<String, String> {

    public KafkaConsumer(@Qualifier("kafkaReceiver") KafkaReceiver<String, String> kafkaReceiver) {
        super(kafkaReceiver);
    }

    @Override
    void process(ReceiverRecord<String, String> stringStringReceiverRecord) {
        log.info("Item Processed.");
    }
}