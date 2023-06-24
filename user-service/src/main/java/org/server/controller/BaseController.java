package org.server.controller;

import javax.annotation.Resource;
import org.server.dao.UserDAO;
import org.server.facade.interfaces.IAuthenticationFacade;
import org.server.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseController {

  @Resource
  private IAuthenticationFacade authenticationFacade;

  @Resource
  private UserService userService;


  protected UserDAO getCurUser() {
    Authentication authentication = authenticationFacade.getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userService.getUserByUsername(userDetails.getUsername());

  }





}
