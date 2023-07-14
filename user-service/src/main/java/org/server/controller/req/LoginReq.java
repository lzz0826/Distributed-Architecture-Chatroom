package org.server.controller.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginReq {

  @ApiModelProperty(value="用户帳號(*必須)")
  private String username;

  @ApiModelProperty(value="用户密碼(*必須)")
  private String password;

}
