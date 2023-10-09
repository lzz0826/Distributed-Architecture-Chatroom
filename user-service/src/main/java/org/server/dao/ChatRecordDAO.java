package org.server.dao;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRecordDAO {

  private String id;

  private String senderUserId;

  private String receiverUserId;

  private String chatroomId;

  private String content;

  private String msgType;

  private String systemMsgType;

  private Boolean status;

  private Date updateTime;

  private Date createTime;



}
