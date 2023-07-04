package org.server.dao;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InterpersonalDAO {

  private String id;

  private String userId;

  private String blacklist;

  private String blacklisted;

  private String banChatRoom;

  private Date updateTime;

  private Date createTime;


}
