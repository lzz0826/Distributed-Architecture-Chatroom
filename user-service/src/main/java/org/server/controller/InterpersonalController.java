package org.server.controller;

import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.interpersonal.AddInsertInterpersonalReq;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotFoundUserException;
import org.server.service.InterpersonalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interpersonal")
public class InterpersonalController {


  @Resource
  private InterpersonalService interpersonalService;

  @PostMapping("/addInsertInterpersonal")
  public BaseResp<String> addInsertInterpersonal(@RequestBody AddInsertInterpersonalReq req)
      throws MissingParameterErrorException {

    if(StringUtils.isBlank(req.getUserId())){
      throw new MissingParameterErrorException();
    }

    String userId = req.getUserId();

    List<String> blacklist = req.getBlacklist();

    List<String> blacklisted = req.getBlacklisted();

    List<String> banChatRoom = req.getBanChatRoom();

    interpersonalService.addInsertInterpersonal(userId,blacklist,blacklisted,banChatRoom);

    return BaseResp.ok(StatusCode.Success);


  }




}
