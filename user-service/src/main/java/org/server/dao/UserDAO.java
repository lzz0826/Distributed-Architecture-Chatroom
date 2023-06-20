package org.server.dao;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDAO {

  private String id ;

  private String username;

  private String password;

  private String address;


}
