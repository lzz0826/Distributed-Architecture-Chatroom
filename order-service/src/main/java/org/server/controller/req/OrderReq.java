package org.server.controller.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReq {

  private String id;

  private String userId;

  private String name;

  private int price;

  private int num ;

}
