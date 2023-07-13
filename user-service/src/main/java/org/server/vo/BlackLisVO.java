package org.server.vo;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackLisVO {

  private String id;

  private String userId;

  private String blacklist;

  private Date updateTime;

  private Date createTime;


}
