package org.server.controller;

import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.annotation.Resource;
import org.server.common.BaseResp;
import org.server.controller.rep.chatroomRecord.ChatRecordListRep;
import org.server.controller.req.chatroomRecord.ListReq;
import org.server.exception.chatroomRecord.NeedPageException;
import org.server.exception.chatroomRecord.NeedPageSizeException;
import org.server.service.ChatRecordService;
import org.server.vo.ChatroomRecordVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "聊天記錄 相關API")
@RestController
@RequestMapping("/chatroomRecord")
public class ChatRecordController {

  @Resource
  private ChatRecordService chatRecordService;

  @PostMapping("/list")
  @ApiOperation("查詢聊天紀錄List")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<ChatRecordListRep> list(@RequestBody @ApiParam("查詢聊天紀錄List請求")ListReq req)
      throws NeedPageException, NeedPageSizeException {

    if(req.getPage() == null){
      throw  new NeedPageException();
    }
    if(req.getPageSize() == null){
      throw new NeedPageSizeException();
    }

    Page<ChatroomRecordVO> vos = chatRecordService.selectChatRecords(req);

    ChatRecordListRep rep = ChatRecordListRep
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
