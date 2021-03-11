package me.ialext.abi.api.test;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class Foo {

  @JsonProperty("poto") int poto;

  @ConstructorProperties("poto")
  public Foo(
      int poto
  ) {
    this.poto = poto;
  }

  public int getPoto() {
    return poto;
  }

}
