package org.server.controller.rep.chatroom;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSilenceCacheRep {

  @ApiModelProperty(value="userId")
  private String userId;

  @ApiModelProperty(value="聊天室id")
  private String chatroomId;



}
