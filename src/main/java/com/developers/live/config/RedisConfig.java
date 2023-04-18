package com.developers.live.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  public String host;

  @Value("${spring.data.redis.port}")
  public int port;

  @Bean
  public RedisConnectionFactory cacheRedisConnectionFactory() {
    RedisClusterConfiguration config = new RedisClusterConfiguration(); config. setMaxRedirects(3);
    config.setClusterNodes(List.of(
            new RedisNode(host, port)
  ));
    return new LettuceConnectionFactory(config);
  }

  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(cacheRedisConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    return redisTemplate;
  }
}
