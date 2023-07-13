package org.server.dao;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class BlackListDAO {

  private String id;

  private String userId;

  private String blacklist;

  private Date updateTime;

  private Date createTime;

}
