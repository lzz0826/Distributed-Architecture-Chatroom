package org.server.controller.req.chatroomRecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.websocket.enums.EWsMsgType;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageReq {

  /**
   * 上傳對象ID
   */
  @ApiModelProperty(value="上傳對象ID(*必須)")
  private String userId;

  @ApiModelProperty(value="私聊對象id(EWsMsgType PrivateChat時為必須)")
  private String receiverUserId;

  @ApiModelProperty(value="聊天室id(EWsMsgType Chatroom時為必須)")
  private String chatroomId;

  @ApiModelProperty(value="聊天類型(私聊:PrivateChat  公告:All  聊天室:Chatroom)")
  private EWsMsgType eWsMsgType;

  @ApiModelProperty(value="圖片 jpg.png (*必須)")
  private MultipartFile file;


}
