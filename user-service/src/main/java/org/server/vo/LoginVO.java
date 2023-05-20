package org.server.vo;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {

  private String jwtToken;


}
