package org.example.kafka;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

public abstract class AbstractConsumer<K, V> {

    protected final KafkaReceiver<K, V> kafkaReceiver;
    protected Disposable disposable;

    protected AbstractConsumer(KafkaReceiver<K, V> kafkaReceiver) {
        this.kafkaReceiver = kafkaReceiver;
    }

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

    public void seekToEnd() {
        kafkaReceiver.doOnConsumer(kvConsumer -> {
            kvConsumer.seekToEnd(kvConsumer.assignment());
            return Mono.empty();
        }).subscribe();
    }

    public void stop() {
        disposable.dispose();
    }
}
