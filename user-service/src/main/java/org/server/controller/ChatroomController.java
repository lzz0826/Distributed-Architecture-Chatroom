package org.server.controller;


import com.github.pagehelper.Page;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.rep.chatroom.JoinChatroomRep;
import org.server.controller.rep.chatroom.ListRep;
import org.server.controller.req.AddChatroomReq;
import org.server.controller.req.GetChatroomByIdReq;
import org.server.controller.req.JoinChatroomReq;
import org.server.dao.ChatroomDAO;
import org.server.dao.UserDAO;
import org.server.exception.AddErrorException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NeedChatroomIdException;
import org.server.exception.NeedChatroomNameException;
import org.server.service.ChatroomService;
import org.server.vo.ChatroomVO;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
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
    String name = req.getName();
    chatroomService.addChatroom(name);
    return BaseResp.ok(StatusCode.Success);
  }



  @PostMapping("/joinChatroom")
  private BaseResp<JoinChatroomRep> joinChatroom(@RequestBody JoinChatroomReq req)
      throws NeedChatroomIdException {
    if (StringUtils.isBlank(req.getId())){
      throw new NeedChatroomIdException();
    }
    String chatroomId = req.getId();
    String userId = req.getUserId();

    if(StringUtils.isBlank(req.getUserId())){
      UserDAO curUser = getCurUser();
      userId = curUser.getId();
    }

    chatroomService.joinChatroom(chatroomId,userId);

    JoinChatroomRep rep = JoinChatroomRep
        .builder()
        .chatroomId(chatroomId)
        .userId(userId)
        .build();
    return BaseResp.ok(rep,StatusCode.Success);


  }



}
