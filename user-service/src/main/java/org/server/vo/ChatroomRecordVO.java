package org.server.vo;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatroomRecordVO {

  @ApiModelProperty(value="聊天室紀錄id")
  private String id;

  @ApiModelProperty(value="發送者id")
  private String senderUserId;

  @ApiModelProperty(value="接收者id")
  private String receiverUserId;

  @ApiModelProperty(value="聊天室id")
  private String chatroomId;

  @ApiModelProperty(value="內容")
  private String content;

  @ApiModelProperty(value="聊天類型")
  private String msgType;

  @ApiModelProperty(value="訊息類型")
  private String systemMsgType;

  @ApiModelProperty(value="聊天室狀態")
  private Boolean status;

  @ApiModelProperty(value="更新時間")
  private Date updateTime;

  @ApiModelProperty(value="創建時間")
  private Date createTime;


}
