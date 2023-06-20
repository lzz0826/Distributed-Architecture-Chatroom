package org.server.dao;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatroomDAO {


  private String id;

  private String name;

  private String adminUserId;

  private Boolean status;

  private Date updateTime;

  private Date createTime;



}
