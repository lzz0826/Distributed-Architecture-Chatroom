package org.server.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.LoginReq;
import org.server.exception.LoginErrorException;
import org.server.exception.UserException;
import org.server.service.UserService;
import org.server.vo.LoginVO;
import org.server.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequestMapping("/user")
public class UserController extends BaseController{

  @GetMapping("/{id}")
  public BaseResp<UserVO> queryById(@PathVariable("id") String id) throws UserException {

    if(StringUtils.isBlank(id)){
      throw new UserException();
    }
    UserVO userVO = userService.gitUserVO(id);
    return BaseResp.ok(userVO, StatusCode.Success);
  }

  @PostMapping("/login")
  public BaseResp<LoginVO> login(@RequestBody() LoginReq loginReq, HttpServletRequest request)
      throws LoginErrorException {
    if(StringUtils.isBlank(loginReq.getUsername())){
      throw new LoginErrorException();
    }

    if(StringUtils.isBlank(loginReq.getPassword())){
      throw new LoginErrorException();
    }



    String username = loginReq.getUsername();
    String password = loginReq.getPassword();
    String ip = request.getRemoteAddr();

    LoginVO login = userService.login(username, password);

    return BaseResp.ok(login,StatusCode.Success);

  }

}
