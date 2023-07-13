package org.server.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.dao.BlackListDAO;
import org.server.exception.BlackListException.AddBlackListException;
import org.server.exception.BlackListException.DelBlackListException;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotAllowedNullStrException;
import org.server.mapper.BlackListMapper;
import org.server.sercice.IdGeneratorService;
import org.server.vo.BlackLisVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BlackListService {

  @Resource
  private BlackListMapper blackListMapper;

  @Resource
  private IdGeneratorService idGeneratorService;

  public List<BlackListDAO> findByBlacklist(String blacklist){
    List<BlackListDAO> daos = blackListMapper.selectByBlacklist(blacklist);
    return daos;
  }


  public List<BlackListDAO> findByUserIdDAOs(String userId){
    List<BlackListDAO> daos = blackListMapper.selectByUserId(userId);
    return daos;
  }

  public List<BlackLisVO> findByUserIdVOs(String userId) {
    List<BlackListDAO> byUserIdDAOs = findByUserIdDAOs(userId);
    List<BlackLisVO> vos = daosToVos(byUserIdDAOs);
    return vos;
  }

  public BlackListDAO addBlackList(String userId , String blackList)
      throws AddBlackListException, MissingParameterErrorException {
    if(StringUtils.isBlank(userId)){
      throw new MissingParameterErrorException();
    }
    if(StringUtils.isBlank(blackList)){
      throw new MissingParameterErrorException();
    }
    BlackListDAO dao = makeDAO(userId, blackList);
    int i = blackListMapper.insertBlackList(dao);

    if(i == 0){
      throw new AddBlackListException();
    }
    return dao;
  }


  public List<BlackLisVO> addBlackLists(String userId , List<String> blackLists)
      throws MissingParameterErrorException, AddBlackListException, NotAllowedNullStrException {

    List<BlackListDAO> daos = new ArrayList<>();

    if(StringUtils.isBlank(userId)){
      throw new MissingParameterErrorException();
    }
    if(blackLists == null || blackLists.isEmpty()){
      throw new MissingParameterErrorException();
    }

    if(blackLists.contains("")){
      throw new NotAllowedNullStrException();
    }

    for (String blackList : blackLists) {
      daos.add(makeDAO(userId,blackList));

    }
    int i = blackListMapper.insertBlackLists(daos);
    if(i == 0){
      throw new AddBlackListException();
    }
    //TODO 如果DB已有會不添加(略過),返回的id會錯
    List<BlackLisVO> vos = daosToVos(daos);
    return vos;
  }

  public List<BlackLisVO> daosToVos(List<BlackListDAO> daos){
    List<BlackLisVO> vos = new ArrayList<>();
    for (BlackListDAO dao : daos) {
      BlackLisVO vo = new BlackLisVO();
      BeanUtils.copyProperties(dao,vo);
      vos.add(vo);
    }
    return vos;
  }


  public BlackListDAO makeDAO(String userId , String blackList){
    BlackListDAO dao = BlackListDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .userId(userId)
        .blacklist(blackList)
        .updateTime(new Date())
        .createTime(new Date())
        .build();
    return dao;
  }


  public void delIds(List<String> ids) throws MissingParameterErrorException, DelBlackListException {
    if(ids == null || ids.isEmpty()){
      throw new MissingParameterErrorException();
    }
    int i = blackListMapper.deleteByIds(ids);
  }






}
