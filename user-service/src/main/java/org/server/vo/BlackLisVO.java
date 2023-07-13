package org.server.vo;


import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackLisVO {

  @ApiModelProperty(value="id")
  private String id;

  @ApiModelProperty(value="userId")
  private String userId;

  @ApiModelProperty(value="黑名單(userId)")
  private String blacklist;

  @ApiModelProperty(value="更新時間")
  private Date updateTime;

  @ApiModelProperty(value="創建時間")
  private Date createTime;


}
