package org.server.vo;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InterpersonalVO {

  private String id;

  private String userId;

  private String blacklist;

  private String blacklisted;

  private String banChatRoom;

  private Date updateTime;

  private Date createTime;



}
