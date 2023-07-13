package org.server.controller.req.blackList;


import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddBlackListReq {

  @ApiModelProperty(value="用户名(必須)")
  private String userId;

  @ApiModelProperty(value="黑名單(必須,List不能含有空字串)")
  private List<String> blackLists;

}
