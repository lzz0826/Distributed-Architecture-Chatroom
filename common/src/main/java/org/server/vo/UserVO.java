package org.server.vo;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserVO {

  private String id ;

  private String username;

  private String address;

  public UserVO() {
  }

  public UserVO(String id, String username, String address) {
    this.id = id;
    this.username = username;
    this.address = address;
  }
}
