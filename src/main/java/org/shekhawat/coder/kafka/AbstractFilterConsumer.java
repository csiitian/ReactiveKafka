package org.shekhawat.coder.kafka;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public abstract class AbstractFilterConsumer <K, V> extends AbstractConsumer<K, V> {

    private final AtomicLong recordProcessed = new AtomicLong(0);
    protected AbstractFilterConsumer(KafkaReceiver<K, V> kafkaReceiver) {
        super(kafkaReceiver);
    }

    @Override
    public void start() {
        disposable = Flux.defer(kafkaReceiver::receive)
                .doOnNext(this::stopConsumer)
                .filter(this::filterRecord)
                .parallel()
                .runOn(Schedulers.newParallel("kafka-filter-consumer-parallel"))
                .doOnNext(this::process)
                .doOnNext(record -> recordProcessed.incrementAndGet())
                .sequential()
                .subscribe();
    }

    protected abstract void stopConsumer(ReceiverRecord<K,V> record);

    @Override
    public void stop() {
        super.stop();
        log.info("Total Record Processed: {}", recordProcessed);
    }

    protected abstract Boolean filterRecord(ReceiverRecord<K, V> record);
}
