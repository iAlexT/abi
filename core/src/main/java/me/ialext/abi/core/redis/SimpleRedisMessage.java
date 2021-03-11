package me.ialext.abi.core.redis;

import me.ialext.abi.api.redis.RedisMessage;

public class SimpleRedisMessage<T> implements RedisMessage<T> {

  private final String channel;
  private final T object;

  public SimpleRedisMessage(
      String channel,
      T object
  ) {
    this.channel = channel;
    this.object = object;
  }

  @Override public String getChannel() {
    return channel;
  }

  @Override public T getObject() {
    return object;
  }
}
