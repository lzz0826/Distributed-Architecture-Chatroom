package org.server.controller.req.chatroom;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetChatroomByIdReq {

  @ApiModelProperty(value="聊天室id(*必須)")
  public String id;

}
