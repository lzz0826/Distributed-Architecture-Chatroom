package org.server.controller;

import java.util.List;
import javax.annotation.Resource;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.blackList.AddBlackListReq;
import org.server.controller.req.blackList.DelIdsReq;
import org.server.exception.BlackListException.AddBlackListException;
import org.server.exception.BlackListException.DelBlackListException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotAllowedNullStrException;
import org.server.service.BlackListService;
import org.server.vo.BlackLisVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blackList")
public class BlackListController extends BaseController{

  @Resource
  private BlackListService blackListService;

  @PostMapping("/addBlackList")
  public BaseResp<List> addBlackList(@RequestBody AddBlackListReq req)
      throws MissingParameterErrorException, AddBlackListException, NotAllowedNullStrException {

    if(StringUtils.isBlank(req.getUserId())){
      throw new MissingParameterErrorException();
    }
    if(req.getBlackLists() == null || req.getBlackLists().isEmpty()){
      throw new MissingParameterErrorException();
    }

    String userId = req.getUserId();
    List<String> blackLists = req.getBlackLists();

    if(blackLists.contains("")){
      throw new NotAllowedNullStrException();
    }


    List<BlackLisVO> blackLisVOS = blackListService.addBlackLists(userId,blackLists);
    return BaseResp.ok(blackLisVOS, StatusCode.Success);

  }


  @PostMapping("/delIds")
  public BaseResp<String> delIds(@RequestBody DelIdsReq req)
      throws MissingParameterErrorException, NotAllowedNullStrException, DelBlackListException {
    if(req.getIds() == null || req.getIds().isEmpty()){
      throw new MissingParameterErrorException();
    }
    List<String> ids = req.getIds();

    if(ids.contains("")){
      throw new NotAllowedNullStrException();
    }
    blackListService.delIds(ids);
    return BaseResp.ok(StatusCode.Success);
  }



}
