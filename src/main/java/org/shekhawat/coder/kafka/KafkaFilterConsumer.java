package org.shekhawat.coder.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
@EnableScheduling
public class KafkaFilterConsumer extends AbstractFilterConsumer<String, String> {

    public KafkaFilterConsumer(@Qualifier("kafkaReceiver") KafkaReceiver<String, String> kafkaReceiver) {
        super(kafkaReceiver);
    }

    @Override
    protected void stopConsumer(ReceiverRecord<String, String> record) {
        if(LocalDateTime.now().minusDays(2).toEpochSecond(ZoneOffset.UTC) > record.timestamp()) {
            super.stop();
        }
    }

    @Override
    protected Boolean filterRecord(ReceiverRecord<String, String> record) {
        return record.value().contains("V-100");
    }

    @Override
    void process(ReceiverRecord<String, String> receiverRecord) {
        log.info("Item Processed. {}", receiverRecord);
    }
}