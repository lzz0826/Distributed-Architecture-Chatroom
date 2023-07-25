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
public class UpdateChatroomReq {

  @ApiModelProperty(value="聊天室id(*必須)")
  private String id;

  @ApiModelProperty(value="聊天室名(非必須)")
  private String name;

  @ApiModelProperty(value="聊天室管理員(非必須)")
  private String adminUserId;

  @ApiModelProperty(value="狀態 開啟:true 關閉:false(非必須)")
  private Boolean status;



}
