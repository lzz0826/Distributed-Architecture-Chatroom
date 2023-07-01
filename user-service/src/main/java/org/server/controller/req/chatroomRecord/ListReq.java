package org.server.controller.req.chatroomRecord;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ListReq {

  private String id;

  //發送者
  private String userId;

  //接收者
  private String receiverUserId;

  //聊天室Id
  private String chatroomId;

  //內容
  private String content;

  //聊天類型 EWsMsgType
  private String msgType;

  //狀態
  private Boolean status;

  //更新時間(起始)
  private Date updateTimeStart;

  //更新時間(結束)
  private Date updateTimeEnd;

  //創建時間(起始)
  private Date createTimeStart;

  //創建時間(結束)
  private Date createTimeEnd;

  //頁碼
  private Integer page;

  //頁碼大小
  private Integer pageSize;

}
