package org.server.dao;


import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRecordDAO {

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
