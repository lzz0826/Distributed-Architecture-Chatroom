package org.server.controller;

import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.interpersonal.AddInsertInterpersonalReq;
import org.server.controller.req.interpersonal.EditInterpersonalReq;
import org.server.exception.Interpersonal.AddInterpersonalFailException;
import org.server.exception.Interpersonal.EditInterpersonalException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotAllowedNullStrException;
import org.server.service.InterpersonalService;
import org.server.vo.InterpersonalVO;
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
  public BaseResp<InterpersonalVO> addInsertInterpersonal(@RequestBody AddInsertInterpersonalReq req)
      throws MissingParameterErrorException, AddInterpersonalFailException {

    if(StringUtils.isBlank(req.getUserId())){
      throw new MissingParameterErrorException();
    }

    String userId = req.getUserId();

    List<String> blacklist = req.getBlacklist();

    List<String> blacklisted = req.getBlacklisted();

    List<String> banChatRoom = req.getBanChatRoom();

    InterpersonalVO vo = interpersonalService.addInsertInterpersonal(userId, blacklist, blacklisted,
        banChatRoom);

    return BaseResp.ok(vo,StatusCode.Success);


  }

  @PostMapping("/editAddInterpersonal")
  public BaseResp<InterpersonalVO> editAddInterpersonal(@RequestBody EditInterpersonalReq req)
      throws MissingParameterErrorException, AddInterpersonalFailException, EditInterpersonalException, NotAllowedNullStrException {


    checkReq(req);

    List<String> blacklist = req.getBlacklist();

    List<String> blacklisted = req.getBlacklisted();

    List<String> banChatRoom = req.getBanChatRoom();

    String userId = req.getUserId();


    InterpersonalVO vo = interpersonalService.editAddInterpersonal(userId, blacklist, blacklisted,
        banChatRoom);

    return BaseResp.ok(vo,StatusCode.Success);


  }


  @PostMapping("/editDelInterpersonal")
  public BaseResp<InterpersonalVO> editDelInterpersonal(@RequestBody EditInterpersonalReq req)
      throws NotAllowedNullStrException, MissingParameterErrorException, EditInterpersonalException {

    checkReq(req);

    List<String> blacklist = req.getBlacklist();

    List<String> blacklisted = req.getBlacklisted();

    List<String> banChatRoom = req.getBanChatRoom();

    String userId = req.getUserId();

    InterpersonalVO vo = interpersonalService.editDelInterpersonal(userId, blacklist, blacklisted,
        banChatRoom);

    return BaseResp.ok(vo,StatusCode.Success);


  }


  private void checkReq(EditInterpersonalReq req ) throws MissingParameterErrorException,
      NotAllowedNullStrException {

    if(StringUtils.isBlank(req.getUserId())){
      throw new MissingParameterErrorException();
    }
    if(req.getBlacklist().contains("")){
      throw new NotAllowedNullStrException();
    }

    if(req.getBlacklisted().contains("")){
      throw new NotAllowedNullStrException();
    }

    if(req.getBanChatRoom().contains("")){
      throw new NotAllowedNullStrException();
    }
  }







}