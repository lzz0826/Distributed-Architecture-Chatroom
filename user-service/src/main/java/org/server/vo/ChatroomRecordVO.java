package org.server.vo;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatroomRecordVO {

  private String id;

  private String userId;

  private String receiverUserId;

  private String chatroomId;

  private String content;

  private String msgType;

  private Boolean status;

  private Date updateTime;

  private Date createTime;


}
