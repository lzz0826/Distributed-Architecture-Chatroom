package org.server.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {


  @ApiModelProperty(value="JWT Token")
  private String jwtToken;

  @ApiModelProperty(value="userId")
  private String userId;




}
