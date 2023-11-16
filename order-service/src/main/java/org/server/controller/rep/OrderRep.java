package org.server.controller.rep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRep {

  private String id;

  private String userId;

  private String name;

  private int price;

  private int num ;

}
