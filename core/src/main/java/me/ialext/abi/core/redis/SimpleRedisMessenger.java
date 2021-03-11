package me.ialext.abi.core.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ialext.abi.api.redis.RedisMessage;
import me.ialext.abi.api.redis.RedisMessenger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.inject.Inject;
import java.io.IOException;

public class SimpleRedisMessenger implements RedisMessenger {

  private final JedisPool jedisPool;
  private final ObjectMapper objectMapper;
  private final JedisPubSub jedisPubSub;

  @Inject public SimpleRedisMessenger(
      JedisPool jedisPool,
      ObjectMapper objectMapper,
      JedisPubSub jedisPubSub
  ) {
    this.jedisPool = jedisPool;
    this.objectMapper = objectMapper;
    this.jedisPubSub = jedisPubSub;
  }

  @Override public <T> void sendMessage(RedisMessage<T> redisMessage) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.publish(redisMessage.getChannel(), objectMapper.writeValueAsString(redisMessage.getObject()));
    } catch (IOException e) {
      System.out.println(
          "An unexpected error has occurred while attempting to send a message through the Redis Channel " + redisMessage.getChannel()
      );
      e.printStackTrace();
    }
  }

  @Override public void subscribe(String... channels) {
    new Thread(() -> {
      try (Jedis jedis = jedisPool.getResource()) {
        jedis.subscribe(
            jedisPubSub,
            channels
        );
      }
    }).start();
  }

}
