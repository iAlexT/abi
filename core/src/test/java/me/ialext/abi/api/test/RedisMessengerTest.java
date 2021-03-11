package me.ialext.abi.api.test;

import me.ialext.abi.core.redis.SimpleRedisMessage;
import me.ialext.abi.core.redis.SimpleRedisMessenger;
import me.ialext.abi.api.redis.RedisMessenger;
import me.yushust.inject.Injector;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPubSub;

public class RedisMessengerTest {

  @Test public void testRedisMessaging() {
    Injector injector = Injector.create(binder -> {
      binder.bind(RedisMessenger.class).to(SimpleRedisMessenger.class).singleton();
      binder.bind(JedisPubSub.class).toInstance(
          new JedisPubSub() {
            @Override public void onMessage(String channel, String message) {
              System.out.println(
                  "The channel '" + channel + "' has received a message! '" + message + "'"
              );
            }

            @Override public void onSubscribe(String channel, int subscribedChannels) {
              System.out.println(
                  "The channel '" + channel + "' has been successfully subscribed. There are '" +
                      subscribedChannels + "' subscribed channels."
              );
            }
          }
      );
    });

    injector.injectMembers(this);

    RedisMessenger redisMessenger = injector.getInstance(RedisMessenger.class);
    redisMessenger.subscribe("test-channel", "test-channel-2", "test-channel-3");
    redisMessenger.sendMessage(new SimpleRedisMessage<>("test-channel", new Foo(12)));
    redisMessenger.sendMessage(new SimpleRedisMessage<>("test-channel-2", new Foo(134)));
    redisMessenger.sendMessage(new SimpleRedisMessage<>("test-channel-3", new Foo(9493)));
  }

}
