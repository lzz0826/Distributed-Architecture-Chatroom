package org.server.controller;


import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.exception.UserException;
import org.server.service.UserService;
import org.server.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/user")
public class UserController {



  @Resource
  private UserService userService;

  @GetMapping("/{id}")
  public BaseResp<UserVO> queryById(@PathVariable("id") String id) throws UserException {

    if(StringUtils.isBlank(id)){
      throw new UserException();
    }

    UserVO userVO = userService.gitUserById(id);
    return BaseResp.ok(userVO, StatusCode.Success);

  }

}
