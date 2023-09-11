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
public class LeaveChatroomReq {


  @ApiModelProperty(value="聊天室ID(*必須)")
  private String id;

  @ApiModelProperty(value="用户名ID(*必須)")
  private String userId;



}
