package org.server.controller.req;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAddReq {


  private String username;

  private String password;

  private String address;



}
