package org.server.pojo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

  private String id;

  private String userId;

  private String name;

  private int price;

  private int num ;






}
