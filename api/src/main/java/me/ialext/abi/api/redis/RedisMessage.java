package me.ialext.abi.api.redis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface RedisMessage<T> {

  @JsonIgnore String getChannel();

  @JsonProperty("object") T getObject();

}
