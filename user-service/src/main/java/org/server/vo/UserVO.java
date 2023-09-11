package org.server.vo;


import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserVO {


  @ApiModelProperty(value="用戶id")
  private String id ;

  @ApiModelProperty(value="用戶帳號")
  private String username;

  @ApiModelProperty(value="頭像地址")
  private String avatarPth;

  @ApiModelProperty(value="用戶地址")
  private String address;

  @ApiModelProperty(value="用戶角色")
  private String role;

  @ApiModelProperty(value="更新時間")
  private Date updateTime;

  @ApiModelProperty(value="創建時間")
  private Date createTime;





}
