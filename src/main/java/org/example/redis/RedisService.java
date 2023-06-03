package org.example.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class RedisService {

    private final RedisRepository redisRepository;

    public RedisService(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    public Mono<String> getData(String key) {
        return redisRepository.findByKey(key);
    }

    public Mono<Boolean> storeData(String key, String data, Duration ttl) {
        return redisRepository.save(key, data, ttl);
    }

    public Mono<Duration> getTtl(String key) {
        return redisRepository.getTtl(key);
    }
}
