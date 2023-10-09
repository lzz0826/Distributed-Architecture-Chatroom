package org.server.controller.req.chatroomRecord;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListReq {

  @ApiModelProperty(value="id(非必須)")
  private String id;

  @ApiModelProperty(value="發送者userId(非必須)")
  private String senderUserId;

  @ApiModelProperty(value="接收者userId(非必須)")
  private String receiverUserId;

  @ApiModelProperty(value="聊天室Id(非必須)")
  private String chatroomId;

  @ApiModelProperty(value="內容(非必須)")
  private String content;

  @ApiModelProperty(value="聊天類型(非必須) 私聊:privateChat 公告:all 聊天室:chatroom")
  private String msgType;

  @ApiModelProperty(value="訊息類型(非必須 System . App . HeartBeat )")
  private String systemMsgType;

  @ApiModelProperty(value="狀態(非必須)")
  private Boolean status;

  @ApiModelProperty(value="更新時間(起始)(非必須)")
  private Date updateTimeStart;

  @ApiModelProperty(value="更新時間(結束)(非必須)")
  private Date updateTimeEnd;

  @ApiModelProperty(value="創建時間(起始)(非必須)")
  private Date createTimeStart;

  @ApiModelProperty(value="創建時間(結束)(非必須)")
  private Date createTimeEnd;

  @ApiModelProperty(value="頁碼(*必須)")
  private Integer page;

  @ApiModelProperty(value="頁碼大小(*必須)")
  private Integer pageSize;

}
