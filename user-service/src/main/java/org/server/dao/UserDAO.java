package org.server.dao;


import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDAO {

  private String id ;

  private String username;

  private String password;

  private String address;

  private String role;

  private Date updateTime;

  private Date createTime;



}
