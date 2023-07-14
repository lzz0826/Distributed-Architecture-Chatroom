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
public class AddChatSilenceCacheReq {

  @ApiModelProperty(value="用户ID(*必須)")
  private String userId;

  @ApiModelProperty(value="聊天室Id(非必須)")
  private String chatroomId ;

  @ApiModelProperty(value="禁言的時間(秒)(*必須)")
  private Integer timeout;

}
