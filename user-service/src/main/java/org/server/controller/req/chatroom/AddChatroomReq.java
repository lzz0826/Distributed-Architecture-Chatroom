package org.server.controller.req.chatroom;


import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddChatroomReq {

  @ApiModelProperty(value="聊天室名稱(*必須)")
  private String name;

  @ApiModelProperty(value="管理員Id(沒帶會使用當前登入UserId)")
  private String adminUserId;

}
