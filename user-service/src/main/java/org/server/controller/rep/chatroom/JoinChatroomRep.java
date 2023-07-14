package org.server.controller.rep.chatroom;


import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinChatroomRep {


  @ApiModelProperty(value="聊天室 id")
  private String chatroomId;

  @ApiModelProperty(value="被加入的userId")
  private String userId;

  @ApiModelProperty(value="管理員Id")
  private String adminUserId;

  @ApiModelProperty(value="聊天室狀態")
  private Boolean status;


}
