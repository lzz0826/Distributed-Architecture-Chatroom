package org.server.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserVO {

  @ApiModelProperty(value="用戶id")
  private String id ;

  @ApiModelProperty(value="用戶帳號")
  private String username;

  @ApiModelProperty(value="用戶地址")
  private String address;





}
