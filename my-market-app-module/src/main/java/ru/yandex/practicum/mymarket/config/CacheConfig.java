package ru.yandex.practicum.mymarket.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import ru.yandex.practicum.mymarket.dto.item.ItemDto;
import ru.yandex.practicum.mymarket.dto.item.ItemPageDto;

import java.time.Duration;

@EnableCaching
@Configuration
@ConditionalOnProperty(prefix = "spring.cache", name = "type", havingValue = "redis")
public class CacheConfig {
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(CacheProperties cacheProperties) {
        RedisCacheConfiguration defaultConfig0 = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(cacheProperties.getRedis().getTimeToLive())
                .prefixCacheNameWith(cacheProperties.getRedis().getKeyPrefix() != null
                        ? cacheProperties.getRedis().getKeyPrefix()
                        : "");

        RedisCacheConfiguration defaultConfig = cacheProperties.getRedis().isCacheNullValues() ? defaultConfig0 :
                defaultConfig0.disableCachingNullValues();

        return builder -> builder
                .withCacheConfiguration("item",
                        defaultConfig
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new Jackson2JsonRedisSerializer<>(ItemDto.class))
                                )
                )
                .withCacheConfiguration("item_pages",
                        defaultConfig
                                .entryTtl(Duration.ofMinutes(10))
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                        .fromSerializer(new Jackson2JsonRedisSerializer<>(ItemPageDto.class))
                                )
                );
    }
}