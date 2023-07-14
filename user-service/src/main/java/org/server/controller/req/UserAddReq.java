package org.server.controller.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAddReq {

  @ApiModelProperty(value="用户名帳號(*必須)")
  private String username;

  @ApiModelProperty(value="用户名密碼(*必須)")
  private String password;

  @ApiModelProperty(value="用户地址(非必須)")
  private String address;


}
