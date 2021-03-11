package me.ialext.abi.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SavableModel {

  @JsonProperty("_id") String getId();

}
