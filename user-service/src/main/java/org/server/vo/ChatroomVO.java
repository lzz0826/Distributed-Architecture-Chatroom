package org.server.vo;


import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatroomVO {

  private String id;

  private String name;

  private String adminUserId;

  private Boolean status;

  private Date updateTime;

  private Date createTime;



}
