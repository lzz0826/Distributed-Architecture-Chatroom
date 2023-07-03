package org.server.controller;


import com.github.pagehelper.Page;
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
  public BaseResp<ListRep> list(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize){
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
  public BaseResp<ChatroomVO> getChatroomById(@RequestBody GetChatroomByIdReq req)
      throws MissingParameterErrorException {
    if(StringUtils.isBlank(req.getId())){
      throw new MissingParameterErrorException();
    }
    String id = req.getId();
    ChatroomVO vo = chatroomService.getChatroomById(id);
    return BaseResp.ok(vo,StatusCode.Success);
  }



  @PostMapping("/addChatroom")
  public BaseResp<String> addChatroom(@RequestBody AddChatroomReq req)
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
  private BaseResp<JoinChatroomRep> joinChatroom(@RequestBody JoinChatroomReq req)
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
  private BaseResp<String> leaveChatroom(@RequestBody LeaveChatroomReq req) throws NeedChatroomIdException {

    String userId = req.getUserId();

    chatroomService.leaveChatroom(userId);

    return BaseResp.ok(StatusCode.Success);

  }

  @PostMapping("/addChatSilenceCache")
  private BaseResp<String> addChatSilenceCache(@RequestBody AddChatSilenceCacheReq req)
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
  public BaseResp<GetSilenceCacheRep> getSilenceCache(@RequestParam String userId)
      throws NotFoundUserException {
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





}
