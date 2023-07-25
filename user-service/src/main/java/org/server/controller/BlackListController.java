package org.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.rep.blackList.BlackListRep;
import org.server.controller.req.blackList.AddBlackListReq;
import org.server.controller.req.blackList.BlackListDelIdsReq;
import org.server.exception.blackListException.AddBlackListException;
import org.server.exception.blackListException.DelBlackListException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotAllowedNullStrException;
import org.server.service.BlackListService;
import org.server.vo.BlackLisVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "黑名單 相關API")
@RestController
@RequestMapping("/blackList")
public class BlackListController extends BaseController{

  @PostMapping("/addBlackList")
  @ApiOperation("新增黑名單")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<BlackListRep> addBlackList(@RequestBody @ApiParam("新增黑名單請求")AddBlackListReq req)
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

    List<BlackLisVO> vos = blackListService.addBlackLists(userId,blackLists);

    BlackListRep blackListRep = BlackListRep
        .builder()
        .blackLis(vos)
        .build();
    return BaseResp.ok(blackListRep,StatusCode.Success);

  }

  @Resource
  private BlackListService blackListService;



  @PostMapping("/delIds")
  @ApiOperation("刪除黑名單")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> delIds(@RequestBody @ApiParam("刪除黑名單請求") BlackListDelIdsReq req)
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

  @GetMapping("/getBlackList")
  @ApiOperation("取得黑名單")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<BlackListRep> getBlackList(@RequestParam() @ApiParam("userId(必須)")  String userId)
      throws MissingParameterErrorException {

    if(StringUtils.isBlank(userId)){
      throw new MissingParameterErrorException();
    }

    List<BlackLisVO> vos = blackListService.findByUserIdVOs(userId);
    BlackListRep blackListRep = BlackListRep
        .builder()
        .blackLis(vos)
        .build();
    return BaseResp.ok(blackListRep,StatusCode.Success);
  }



}
