package org.example.redis;

import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class RedisRepository {

    private final ReactiveRedisOperations<String, String> reactiveRedisOperations;

    public RedisRepository(ReactiveRedisOperations<String, String> reactiveRedisOperations) {
        this.reactiveRedisOperations = reactiveRedisOperations;
    }

    public Mono<Boolean> save(String key, String data, Duration ttl) {
        return reactiveRedisOperations.opsForValue().set(key, data, ttl);
    }

    public Mono<String> findByKey(String key) {
        return reactiveRedisOperations.opsForValue().get(key);
    }

    public Mono<Duration> getTtl(String key) {
        return reactiveRedisOperations.getExpire(key);
    }
}
