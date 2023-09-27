package org.server.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatSilenceCacheVO {

  @ApiModelProperty(value="用戶id")
  private String userId;

  @ApiModelProperty(value="被禁言聊天室id")
  private String chatroomId ;

  @ApiModelProperty(value="靜言到期時間")
  private long timeout;

}
