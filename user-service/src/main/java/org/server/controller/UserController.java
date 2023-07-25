package org.server.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.rep.LogoutRep;
import org.server.controller.req.LoginReq;
import org.server.controller.req.UserAddReq;
import org.server.exception.AddErrorException;
import org.server.exception.LoginErrorException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.UserException;
import org.server.service.ChatroomService;
import org.server.service.JwtCacheService;
import org.server.service.UserService;
import org.server.vo.LoginVO;
import org.server.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "用戶相關API")
@RestController
@RequestMapping("/user")
public class UserController{

  @Resource
  private UserService userService;

  @Resource
  private JwtCacheService jwtCacheService;


  @PostMapping("/addUser")
  @ApiOperation("新增用戶")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> addUser(@RequestBody  @ApiParam("新增用戶請求") UserAddReq userAddReq)
      throws AddErrorException, MissingParameterErrorException {
    if(StringUtils.isBlank(userAddReq.getUsername())){
      throw new MissingParameterErrorException();
    }

    if(StringUtils.isBlank(userAddReq.getPassword())){
      throw new MissingParameterErrorException();
    }

    String username = userAddReq.getUsername();
    String password = userAddReq.getPassword();
    String address = userAddReq.getAddress();

    userService.addUser(username,password,address);

    return BaseResp.ok(StatusCode.Success);


  }


  @GetMapping("/{id}")
  @ApiOperation("查詢用戶")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<UserVO> queryById(@PathVariable("id") @ApiParam("用戶id") String id) throws UserException {

    if(StringUtils.isBlank(id)){
      throw new UserException();
    }
    UserVO userVO = userService.getUserVO(id);
    return BaseResp.ok(userVO, StatusCode.Success);
  }

  @GetMapping("/gitAllUser")
  @ApiOperation("查詢所有用戶")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<List<UserVO>> gitAllUser(){

    List<UserVO> allUserVOs = userService.getAllUserVOs();

    return BaseResp.ok(allUserVOs,StatusCode.Success);



  }

  @PostMapping("/login")
  @ApiOperation("登入")
  public BaseResp<LoginVO> login(@RequestBody() @ApiParam("登入請求") LoginReq loginReq,
      HttpServletRequest request)
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


  @PostMapping("/logout")
  @ApiOperation("登出")
  public BaseResp<LogoutRep> logout(@RequestHeader("Authorization")  @ApiParam("登出請求") String jwtToken) {
    //TODO 登出
    jwtCacheService.deleteTokenByJwtToken(jwtToken);

    return BaseResp.ok(LogoutRep.builder().build());
  }

}
