package org.server.controller;

import com.github.pagehelper.Page;
import java.util.List;
import javax.annotation.Resource;
import org.server.common.BaseResp;
import org.server.controller.rep.chatroomRecord.ListRep;
import org.server.controller.req.chatroomRecord.ListReq;
import org.server.dao.ChatRecordDAO;
import org.server.exception.chatroomRecord.NeedPageException;
import org.server.exception.chatroomRecord.NeedPageSizeException;
import org.server.service.ChatRecordService;
import org.server.vo.ChatroomRecordVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatroomRecord")
public class ChatRecordController {

  @Resource
  private ChatRecordService chatRecordService;

  @PostMapping("/list")
  public BaseResp<ListRep> list(@RequestBody ListReq req)
      throws NeedPageException, NeedPageSizeException {

    if(req.getPage() == null){
      throw  new NeedPageException();
    }
    if(req.getPageSize() == null){
      throw new NeedPageSizeException();
    }

    Page<ChatroomRecordVO> vos = chatRecordService.selectChatRecords(req);

    ListRep rep = ListRep
        .builder()
        .chatroomRecords(vos)
        .build();
    rep.setPage(vos.getPageNum());
    rep.setPageSize(vos.getPageSize());
    rep.setTotal(vos.getTotal());
    rep.setTotalPage(vos.getPages());

    return BaseResp.ok(rep);


  }

}
