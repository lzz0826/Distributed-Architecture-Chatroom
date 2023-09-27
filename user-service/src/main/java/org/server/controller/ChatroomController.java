package org.server.controller;


import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.rep.chatroom.AddCacheRoomRep;
import org.server.controller.rep.chatroom.GetChatroomByIdRep;
import org.server.controller.rep.chatroom.GetChatroomOnlineUserListRep;
import org.server.controller.rep.chatroom.GetSilenceCacheRep;
import org.server.controller.rep.chatroom.JoinChatroomRep;
import org.server.controller.rep.chatroom.ChatroomListRep;
import org.server.controller.rep.chatroom.UpdateCacheRoomRep;
import org.server.controller.req.chatroom.AddChatroomReq;
import org.server.controller.req.chatroom.ChatroomDelIdsReq;
import org.server.controller.req.chatroom.JoinChatroomReq;
import org.server.controller.req.chatroom.LeaveChatroomAllReq;
import org.server.controller.req.chatroom.LeaveChatroomReq;
import org.server.controller.req.chatroom.UpdateChatroomReq;
import org.server.controller.req.chatroomRecord.AddChatSilenceCacheReq;
import org.server.controller.req.chatroomRecord.DelSilenceCacheAllReq;
import org.server.controller.req.chatroomRecord.DelSilenceCacheReq;
import org.server.dao.ChatSilenceCacheDAO;
import org.server.dao.UserDAO;
import org.server.exception.AddErrorException;
import org.server.exception.NotAllowedNullStrException;
import org.server.exception.NotFoundUserException;
import org.server.exception.chatroom.ChatroomNotOpenException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.chatroom.NeedChatroomIdException;
import org.server.exception.chatroom.NeedChatroomNameException;
import org.server.exception.chatroom.NotFoundChatroomException;
import org.server.exception.chatroom.UpdateChatroomFailException;
import org.server.service.ChatSilenceCacheService;
import org.server.service.ChatroomService;
import org.server.service.UserService;
import org.server.vo.ChatSilenceCacheVO;
import org.server.vo.ChatroomOnlineUserVO;
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
  public BaseResp<ChatroomListRep> list(@RequestParam("page")@ApiParam("頁碼(*必須)") Integer page,
      @RequestParam("pageSize")@ApiParam("每頁顯示大小(*必須)") Integer pageSize){
    Page<ChatroomVO> vos = chatroomService.getChatroomAll(page,pageSize);
    ChatroomListRep rep = ChatroomListRep
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
  public BaseResp<GetChatroomByIdRep> getChatroomById(@RequestParam("id") @ApiParam("聊天室id") String id)
      throws MissingParameterErrorException {
    if(StringUtils.isBlank(id)){
      throw new MissingParameterErrorException();
    }
    ChatroomVO vo = chatroomService.getChatroomById(id);

    GetChatroomByIdRep rep = GetChatroomByIdRep
        .builder()
        .chatroomVO(vo)
        .build();

    return BaseResp.ok(rep,StatusCode.Success);
  }

  @GetMapping("/getChatroomOnlineUserList")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<GetChatroomOnlineUserListRep> getChatroomOnlineUserList(@RequestParam("id")
  @ApiParam("聊天室id(*必須)") String id ){


    ChatroomOnlineUserVO vo = chatroomService.getChatroomOnlineUserList(id);

    GetChatroomOnlineUserListRep rep = GetChatroomOnlineUserListRep
        .builder()
        .chatroomOnlineUser(vo)
        .build();

    return BaseResp.ok(rep,StatusCode.Success);
  }




  @PostMapping("/addChatroom")
  @ApiOperation("新增聊天室")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<AddCacheRoomRep> addChatroom(@RequestBody @ApiParam("新增聊天室請求") AddChatroomReq req)
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

    ChatroomVO vo = chatroomService.addChatroom(name, adminUserId);

    AddCacheRoomRep rep = AddCacheRoomRep
        .builder()
        .chatroomVO(vo)
        .build();

    return BaseResp.ok(rep,StatusCode.Success);
  }

  @PostMapping("/updateChatroom")
  @ApiOperation("更新聊天室")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  private BaseResp<UpdateCacheRoomRep> updateChatroom(@RequestBody @ApiParam("更新聊天室請求") UpdateChatroomReq req)
      throws MissingParameterErrorException, UpdateChatroomFailException {
    if(StringUtils.isBlank(req.getId())){
      throw new MissingParameterErrorException();
    }
    String id = req.getId();

    ChatroomVO chatroomById = chatroomService.getChatroomById(id);
    if(chatroomById == null){
      throw new NotFoundChatroomException();
    }
    String name = req.getName();
    String adminUserId = req.getAdminUserId();

    Boolean status = req.getStatus();
    ChatroomVO vo = chatroomService.updateChatroom(id, name, adminUserId, status);

    UpdateCacheRoomRep rep = UpdateCacheRoomRep
        .builder()
        .chatroomVO(vo)
        .build();
    return BaseResp.ok(rep,StatusCode.Success);

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
  private BaseResp<String> leaveChatroom(@RequestBody @ApiParam("離開聊天室請求") LeaveChatroomReq req)
      throws MissingParameterErrorException {
    if(StringUtils.isBlank(req.getId())){
      throw new MissingParameterErrorException();
    }

    if(StringUtils.isBlank(req.getUserId())){
      throw new MissingParameterErrorException();
    }

    String chatRoomId = req.getId();

    String userId = req.getUserId();


    chatroomService.leaveChatroom(chatRoomId,userId);

    return BaseResp.ok(StatusCode.Success);

  }

  @PostMapping("/leaveChatroomAll")
  @ApiOperation("離開聊天室(全部)")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  private BaseResp<String> leaveChatroomAll(@RequestBody @ApiParam("離開聊天室請求") LeaveChatroomAllReq req) {

    String userId = req.getUserId();

    chatroomService.leaveChatroomAll(userId);

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
    if(StringUtils.isBlank(userId)){
      throw new NotFoundUserException();
    }
    Map<String, ChatSilenceCacheVO> vos = chatSilenceCacheService.ChatSilenceCacheDaoToVo(userId);

    GetSilenceCacheRep rep = GetSilenceCacheRep.builder().build();

    rep.setSilenceChatroomDatas(vos);
    return BaseResp.ok(rep,StatusCode.Success);

  }

  @PostMapping("/delSilenceCache")
  @ApiOperation("移除禁言人員")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> delSilenceCache(@RequestBody @ApiParam("移除禁言人員請求") DelSilenceCacheReq req)
      throws NotFoundUserException, MissingParameterErrorException {
    if(StringUtils.isBlank(req.getUserId())){
      throw new NotFoundUserException();
    }

    if(StringUtils.isBlank(req.getChatroomId())){
      throw new MissingParameterErrorException();
    }
    String userId = req.getUserId();

    String chatroomId = req.getChatroomId();

    chatSilenceCacheService.delSilenceCacheByUserId(userId,chatroomId);

    return BaseResp.ok(StatusCode.Success);

  }

  @PostMapping("/delSilenceCacheAll")
  @ApiOperation("移除禁言人員(全部)")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> delSilenceCacheAll(@RequestBody @ApiParam("移除禁言人員請求(全部)") DelSilenceCacheAllReq req)
      throws NotFoundUserException {
    if(StringUtils.isBlank(req.getUserId())){
      throw new NotFoundUserException();
    }
    String userId = req.getUserId();

    chatSilenceCacheService.delSilenceCacheByUserIdAll(userId);

    return BaseResp.ok(StatusCode.Success);

  }

  @PostMapping("/delIds")
  @ApiOperation("刪除聊天室")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> delIds(@RequestBody @ApiParam("刪除聊天室請求") ChatroomDelIdsReq req)
      throws MissingParameterErrorException, NotAllowedNullStrException {
    if(req.getIds() == null || req.getIds().isEmpty()){
      throw new MissingParameterErrorException();
    }
    List<String> ids = req.getIds();

    if(ids.contains("")){
      throw new NotAllowedNullStrException();
    }
    chatroomService.delIds(ids);
    return BaseResp.ok(StatusCode.Success);

  }






}
