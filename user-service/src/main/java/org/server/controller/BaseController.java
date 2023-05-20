package org.server.controller;

import javax.annotation.Resource;
import org.server.service.UserService;

public class BaseController {


  @Resource
  protected UserService userService;

}
