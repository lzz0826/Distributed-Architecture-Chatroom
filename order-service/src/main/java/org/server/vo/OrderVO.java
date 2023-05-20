package org.server.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderVO {

  private String id;

  private String userId;

  private String name;

  private int price;

  private int num ;

  private UserVO userVO;

}
