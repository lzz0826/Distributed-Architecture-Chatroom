package org.server.controller;


import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.AddChatroomReq;
import org.server.exception.AddErrorException;
import org.server.exception.NeedChatroomNameException;
import org.server.service.ChatroomService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chatroom")
public class ChatroomController {


  @Resource
  private ChatroomService chatroomService;



  @PostMapping("addChatroom")
  public BaseResp<String> addChatroom(@RequestBody AddChatroomReq req)
      throws NeedChatroomNameException, AddErrorException {
    if(StringUtils.isBlank(req.getName())){
      throw new NeedChatroomNameException();
    }
    String name = req.getName();
    chatroomService.addChatroom(name);

    return BaseResp.ok(StatusCode.Success);

  }

}
