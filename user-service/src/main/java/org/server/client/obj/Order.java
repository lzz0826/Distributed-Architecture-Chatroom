package org.server.client.obj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.vo.UserVO;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

  private String id;

  private String userId;

  private String name;

  private int price;

  private int num ;

  private UserVO userVO;


}
