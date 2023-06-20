package org.server.dao;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDAO {

  private String id;

  private String userId;

  private String name;

  private int price;

  private int num ;






}
