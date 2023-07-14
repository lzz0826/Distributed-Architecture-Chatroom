package org.server.controller;


import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.rep.chatroom.GetSilenceCacheRep;
import org.server.controller.rep.chatroom.JoinChatroomRep;
import org.server.controller.rep.chatroom.ListRep;
import org.server.controller.req.chatroom.AddChatroomReq;
import org.server.controller.req.chatroom.GetChatroomByIdReq;
import org.server.controller.req.chatroom.JoinChatroomReq;
import org.server.controller.req.LeaveChatroomReq;
import org.server.controller.req.chatroomRecord.AddChatSilenceCacheReq;
import org.server.controller.req.chatroomRecord.DelSilenceCacheReq;
import org.server.dao.UserDAO;
import org.server.exception.AddErrorException;
import org.server.exception.NotFoundUserException;
import org.server.exception.chatroom.ChatroomNotOpenException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.chatroom.NeedChatroomIdException;
import org.server.exception.chatroom.NeedChatroomNameException;
import org.server.service.ChatSilenceCacheService;
import org.server.service.ChatroomService;
import org.server.service.UserService;
import org.server.vo.ChatroomVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "聊天室 相關API")
@RestController
@RequestMapping("/chatroom")
public class ChatroomController extends BaseController {


  @Resource
  private ChatroomService chatroomService;

  @Resource
  private ChatSilenceCacheService chatSilenceCacheService;

  @Resource
  private UserService userService;



  @GetMapping("/list")
  @ApiOperation("查詢聊天室List")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<ListRep> list(@RequestParam("page")@ApiParam("頁碼(*必須)") Integer page,
      @RequestParam("pageSize")@ApiParam("每頁顯示大小(*必須)") Integer pageSize){
    Page<ChatroomVO> vos = chatroomService.getChatroomAll(page,pageSize);
    ListRep rep = ListRep
        .builder()
        .chatrooms(vos)
        .build();
    rep.setPage(vos.getPageNum());
    rep.setPageSize(vos.getPageSize());
    rep.setTotal(vos.getTotal());
    rep.setTotalPage(vos.getPages());

    return BaseResp.ok(rep);


  }

  @GetMapping("/getChatroomById")
  @ApiOperation("查詢聊天室BY ID")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<ChatroomVO> getChatroomById(@RequestBody @ApiParam("查詢聊天室請求") GetChatroomByIdReq req)
      throws MissingParameterErrorException {
    if(StringUtils.isBlank(req.getId())){
      throw new MissingParameterErrorException();
    }
    String id = req.getId();
    ChatroomVO vo = chatroomService.getChatroomById(id);
    return BaseResp.ok(vo,StatusCode.Success);
  }



  @PostMapping("/addChatroom")
  @ApiOperation("新增聊天室")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> addChatroom(@RequestBody @ApiParam("新增聊天室請求") AddChatroomReq req)
      throws NeedChatroomNameException, AddErrorException {
    if(StringUtils.isBlank(req.getName())){
      throw new NeedChatroomNameException();
    }

    String adminUserId = req.getAdminUserId();
    String name = req.getName();


    if(StringUtils.isBlank(req.getAdminUserId())){
      UserDAO curUser = getCurUser();
      adminUserId = curUser.getUsername();
    }


    chatroomService.addChatroom(name,adminUserId);
    return BaseResp.ok(StatusCode.Success);
  }



  @PostMapping("/joinChatroom")
  @ApiOperation("加入聊天室")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  private BaseResp<JoinChatroomRep> joinChatroom(@RequestBody @ApiParam("加入聊天室請求") JoinChatroomReq req)
      throws NeedChatroomIdException, ChatroomNotOpenException {
    if (StringUtils.isBlank(req.getId())){
      throw new NeedChatroomIdException();
    }
    String chatroomId = req.getId();
    String userId = req.getUserId();

    if(StringUtils.isBlank(req.getUserId())){
      UserDAO curUser = getCurUser();
      userId = curUser.getId();
    }

    ChatroomVO vo = chatroomService.joinChatroom(chatroomId, userId);

    JoinChatroomRep rep = JoinChatroomRep
        .builder()
        .chatroomId(vo.getId())
        .userId(userId)
        .adminUserId(vo.getAdminUserId())
        .status(vo.getStatus())
        .build();
    return BaseResp.ok(rep,StatusCode.Success);


  }


  @PostMapping("/leaveChatroom")
  @ApiOperation("離開聊天室")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  private BaseResp<String> leaveChatroom(@RequestBody @ApiParam("離開聊天室請求") LeaveChatroomReq req) {

    String userId = req.getUserId();

    chatroomService.leaveChatroom(userId);

    return BaseResp.ok(StatusCode.Success);

  }

  @PostMapping("/addChatSilenceCache")
  @ApiOperation("新增禁言")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  private BaseResp<String> addChatSilenceCache(@RequestBody @ApiParam("新增禁言請求") AddChatSilenceCacheReq req)
      throws NotFoundUserException, MissingParameterErrorException {

    if(StringUtils.isBlank(req.getUserId())){
      throw new NotFoundUserException();
    }

    if (req.getTimeout() == null || req.getTimeout() <= 0 || req.getTimeout() % 1 != 0) {
      throw new MissingParameterErrorException();
    }

    String userId = req.getUserId();

    String chatroomId = req.getChatroomId();

    Integer timeout = req.getTimeout();


    UserDAO dao = userService.getUserById(req.getUserId());
    if(dao == null){
      throw new NotFoundUserException();
    }

    chatSilenceCacheService.addChatSilenceCacheByUserId(userId,chatroomId,timeout);

    return BaseResp.ok(StatusCode.Success);

  }


  @GetMapping("/getSilenceCache")
  @ApiOperation("查詢是否被禁言")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<GetSilenceCacheRep> getSilenceCache(@RequestParam @ApiParam("用戶Id*(必須)") String userId)
      throws NotFoundUserException {
    //TODO 尚未實現針對聊天室靜言(目前全禁)
    if(StringUtils.isBlank(userId)){
      throw new NotFoundUserException();
    }
    String chatroomId = chatSilenceCacheService.getSilenceCacheByUserId(userId);

    GetSilenceCacheRep rep = GetSilenceCacheRep.builder().build();
    if(StringUtils.isBlank(chatroomId)){
      return BaseResp.ok(rep,StatusCode.Success);
    }
    rep.setUserId(userId);
    rep.setChatroomId(chatroomId);
    return BaseResp.ok(rep,StatusCode.Success);

  }

  @PostMapping("/delSilenceCache")
  @ApiOperation("移除禁言人員")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> delSilenceCache(@RequestBody @ApiParam("移除禁言人員請求") DelSilenceCacheReq req)
      throws NotFoundUserException {
    if(StringUtils.isBlank(req.getUserId())){
      throw new NotFoundUserException();
    }
    String userId = req.getUserId();

    chatSilenceCacheService.delSilenceCacheByUserId(userId);

    return BaseResp.ok(StatusCode.Success);

    

  }





}
