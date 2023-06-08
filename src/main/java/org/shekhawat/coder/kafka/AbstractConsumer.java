package org.shekhawat.coder.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class AbstractConsumer<K, V> {

    protected final KafkaReceiver<K, V> kafkaReceiver;
    protected Disposable disposable;
    protected Integer rateLimit;
    protected Duration period;

    protected AbstractConsumer(KafkaReceiver<K, V> kafkaReceiver) {
        this.kafkaReceiver = kafkaReceiver;
    }

    protected AbstractConsumer(KafkaReceiver<K, V> kafkaReceiver, Integer rateLimit, Duration period) {
        this(kafkaReceiver);
        this.rateLimit = rateLimit;
        this.period = period;
    }

    public void start() {
        Flux<ReceiverRecord<K, V>> flux = Flux.defer(kafkaReceiver::receive);
        if(rateLimit != null && period != null) {
            flux = flux.limitRate(rateLimit)
                    .delayElements(period.dividedBy(rateLimit));
        }
        disposable = flux
                .parallel()
                .runOn(Schedulers.newParallel("kafka-consumer-parallel"))
                .doOnNext(this::process)
                .sequential()
                .subscribe();
    }

    abstract void process(ReceiverRecord<K,V> kvReceiverRecord);

    public void pause() {
        kafkaReceiver.doOnConsumer(kvConsumer -> {
            kvConsumer.pause(kvConsumer.assignment());
            return Mono.empty();
        }).subscribe();
    }

    public void resume() {
        kafkaReceiver.doOnConsumer(kvConsumer -> {
            kvConsumer.resume(kvConsumer.assignment());
            return Mono.empty();
        }).subscribe();
    }

    public void stop() {
        disposable.dispose();
    }

    public void seekToEnd() {
        kafkaReceiver.doOnConsumer(kvConsumer -> {
            kvConsumer.seekToEnd(kvConsumer.assignment());
            return Mono.empty();
        }).subscribe();
    }

    public void seekToBeginning() {
        kafkaReceiver.doOnConsumer(kvConsumer -> {
            kvConsumer.seekToBeginning(kvConsumer.assignment());
            return Mono.empty();
        }).subscribe();
    }

    public void seekToTimestamp(long timestamp) {
        kafkaReceiver.doOnConsumer(kvConsumer -> {
            Set<TopicPartition> assignment = kvConsumer.assignment();
            Map<TopicPartition, Long> time = new HashMap<>();
            assignment.forEach(topicPartition -> time.put(topicPartition, timestamp));
            Map<TopicPartition, OffsetAndTimestamp> offset = kvConsumer.offsetsForTimes(time);
            offset.forEach(((topicPartition, offsetAndTimestamp) -> {
                kvConsumer.seek(topicPartition, offsetAndTimestamp.offset());
            }));
            return Mono.empty();
        }).subscribe();
    }
}
