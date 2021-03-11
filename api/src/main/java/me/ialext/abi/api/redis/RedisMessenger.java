package me.ialext.abi.api.redis;

public interface RedisMessenger {

  <T> void sendMessage(RedisMessage<T> redisMessage);

  void subscribe(String... channels);

}
