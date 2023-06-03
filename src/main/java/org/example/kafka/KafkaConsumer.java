package org.example.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@EnableScheduling
public class KafkaConsumer extends AbstractConsumer<String, String> {

    private AtomicInteger rateLimit = new AtomicInteger(10);

    public KafkaConsumer(@Qualifier("kafkaReceiver") KafkaReceiver<String, String> kafkaReceiver) {
        super(kafkaReceiver);
    }

    public void startConsumer() {
        disposable = Flux.defer(kafkaReceiver::receive)
                .log("Consumer Log")
                .limitRate(rateLimit.get())
                .delayElements(Duration.ofMillis(1000L / rateLimit.get()))
                .parallel()
                .runOn(Schedulers.newParallel("kafka-consumer-parallel"))
                .doOnNext(this::process)
                .sequential()
                .subscribe();
    }

    private void process(ReceiverRecord<String, String> stringStringReceiverRecord) {
        long startTimestamp = System.currentTimeMillis();
        for(int i=0;i<Integer.MAX_VALUE;i++) {
            recursiveOperation(i);
        }
        long endTimestamp = System.currentTimeMillis();
        log.info("Processing Time: {}", endTimestamp-startTimestamp);
    }

    private void recursiveOperation(int i) {
        Long res = 1L;
        while(i > 0) {
            res *= i;
        }
    }
}