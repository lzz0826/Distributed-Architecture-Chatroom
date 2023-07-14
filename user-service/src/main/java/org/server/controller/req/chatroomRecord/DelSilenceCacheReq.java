package org.server.controller.req.chatroomRecord;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelSilenceCacheReq {

  @ApiModelProperty(value="用户id(*必須)")
  private String userId;

}
