package org.server.controller.req.chatroom;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinChatroomReq {

  @ApiModelProperty(value="聊天室id(*必須)")
  private String id;

  @ApiModelProperty(value="被加入的userId(沒帶會加入當前的登入者)")
  private String userId;


}
