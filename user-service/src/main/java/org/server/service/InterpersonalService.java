package org.server.service;

import static org.server.util.StringUtil.strListToString;
import static org.server.util.StringUtil.strSetToString;
import static org.server.util.StringUtil.stringToStrList;
import static org.server.util.StringUtil.stringToStrSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.dao.InterpersonalDAO;
import org.server.exception.Interpersonal.AddInterpersonalFailException;
import org.server.exception.Interpersonal.EditInterpersonalException;
import org.server.mapper.InterpersonalMapper;
import org.server.sercice.IdGeneratorService;
import org.server.vo.InterpersonalVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterpersonalService {

  @Resource
  private InterpersonalMapper interpersonalMapper;

  @Resource
  private IdGeneratorService idGeneratorService;


  public InterpersonalDAO findByUserId(String userId){
    return interpersonalMapper.selectByUserId(userId);
  }



  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public InterpersonalVO addInsertInterpersonal(String userId, List<String> blacklist,
      List<String> blacklisted, List<String> banChatRoom) throws AddInterpersonalFailException {

    String blacklistStr = null;

    if (blacklist != null && !blacklist.isEmpty()) {
      blacklistStr = strListToString(blacklist);
    }

    String blacklistedStr = null;
    if(blacklisted != null && !blacklisted.isEmpty()){
      blacklistedStr = strListToString(blacklisted);
    }

    String banChatRoomStr = null;
    if(banChatRoom != null && !banChatRoom.isEmpty()){
      banChatRoomStr = strListToString(banChatRoom);
    }

    InterpersonalDAO dao = InterpersonalDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .userId(userId)
        .blacklist(blacklistStr)
        .blacklisted(blacklistedStr)
        .banChatRoom(banChatRoomStr)
        .updateTime(new Date())
        .createTime(new Date())
        .build();

    int i = interpersonalMapper.insertInterpersonal(dao);

    if(i == 0){
      throw new AddInterpersonalFailException();

    }

    InterpersonalVO vo = InterpersonalVO.builder().build();
    BeanUtils.copyProperties(dao,vo);
    return vo;
  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public InterpersonalVO editAddInterpersonal(String userId, List<String> blacklist,
      List<String> blacklisted, List<String> banChatRoom)
      throws AddInterpersonalFailException, EditInterpersonalException {

    InterpersonalVO vo = InterpersonalVO.builder().build();
    InterpersonalDAO dao = findByUserId(userId);
    if(dao == null){
      vo = addInsertInterpersonal(userId, blacklist, blacklisted, banChatRoom);
      if(blacklist != null || !blacklist.isEmpty()){
        addBlacklist(blacklist,userId);
      }
    }else {

      String id = dao.getId();

      Set<String> blacklistSet = stringToStrSet(dao.getBlacklist());

      Set<String> blacklistedSet = stringToStrSet(dao.getBlacklisted());

      Set<String> banChatRoomSet= stringToStrSet(dao.getBanChatRoom());

      blacklistSet.addAll(blacklist);
      blacklistedSet.addAll(blacklisted);
      banChatRoomSet.addAll(banChatRoom);

      InterpersonalDAO updateDao = InterpersonalDAO
          .builder()
          .id(id)
          .blacklist(strSetToString(blacklistSet))
          .blacklisted(strSetToString(blacklistedSet))
          .banChatRoom(strSetToString(banChatRoomSet))
          .updateTime(new Date())
          .build();
      int update = interpersonalMapper.update(updateDao);

      if(update == 0){
        throw new EditInterpersonalException();
      }

      BeanUtils.copyProperties(updateDao,vo);
      vo.setUserId(dao.getUserId());
      vo.setCreateTime(dao.getCreateTime());
      // 添加對方的被黑名單
      if(blacklist != null || !blacklist.isEmpty()){
        addBlacklist(blacklist,userId);
      }

    }
    return vo;
  }




  /**
   * 添加被黑名單對象的表
   * @param blacklistList 要添加的對象名單
   * @param userId 需要被添加的userId
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void addBlacklist(List<String> blacklistList,String userId)
      throws AddInterpersonalFailException {

    for (String setUserid : blacklistList) {
      InterpersonalDAO dao = findByUserId(setUserid);
      if(dao == null ){
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        addInsertInterpersonal(setUserid,null,userIdList,null);

      }else {
        Set<String> blacklists = stringToStrSet(dao.getBlacklist());
        blacklists.add(userId);

        String id = dao.getId();
        InterpersonalDAO updateDao = InterpersonalDAO
            .builder()
            .id(id)
            .blacklisted(strSetToString(blacklists))
            .build();
        interpersonalMapper.update(updateDao);
      }
    }


  }

  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public InterpersonalVO editDelInterpersonal(String userId, List<String> blacklist,
      List<String> blacklisted, List<String> banChatRoom)
      throws  EditInterpersonalException {

    InterpersonalVO vo = InterpersonalVO.builder().build();

    InterpersonalDAO dao = interpersonalMapper.selectByUserId(userId);

    if(dao == null){
      throw new EditInterpersonalException();
    }else {

      String id = dao.getId();

      List<String> blacklistOld = stringToStrList(dao.getBlacklist());
      List<String> blacklistUp = removeList(blacklistOld,blacklist);

      List<String> blacklistedOld = stringToStrList(dao.getBlacklisted());
      List<String> blacklistedUp = removeList(blacklistedOld,blacklisted);

      List<String> banChatRoomOld = stringToStrList(dao.getBanChatRoom());
      List<String> banChatRoomUp = removeList(banChatRoomOld,banChatRoom);

      InterpersonalDAO updateDao = InterpersonalDAO
          .builder()
          .id(id)
          .blacklist(strListToString(blacklistUp))
          .blacklisted(strListToString(blacklistedUp))
          .banChatRoom(strListToString(banChatRoomUp))
          .updateTime(new Date())
          .build();
      int update = interpersonalMapper.update(updateDao);

      if(update == 0){
        throw new EditInterpersonalException();
      }

      InterpersonalDAO newDao = interpersonalMapper.selectById(updateDao.getId());

      BeanUtils.copyProperties(newDao,vo);
      vo.setUserId(dao.getUserId());
      vo.setCreateTime(dao.getCreateTime());

      if(blacklist != null || !blacklist.isEmpty()){
        System.out.println("blacklist : "+blacklist);
        delBlacklist(blacklist,userId);
      }
    }
    return vo;
  }

  /**
   * 刪除被黑名單對象的表
   * @param blacklistList 要添加的對象名單
   * @param userId 需要被移除的userId
   */
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
  public void delBlacklist(List<String> blacklistList ,String userId ){
    for (String id : blacklistList) {
      InterpersonalDAO dao = findByUserId(id);
      System.out.println("dao : " + dao);
      if (dao != null) {
        Set<String> blacklisted = stringToStrSet(dao.getBlacklisted());
        blacklisted.remove(userId);

        System.out.println("blacklistedddd dd : "+blacklisted);
        InterpersonalDAO updateDao = InterpersonalDAO
            .builder()
            .id(dao.getUserId())
            .blacklisted(strSetToString(blacklisted))
            .build();
        interpersonalMapper.update(updateDao);
      }
    }
  }

  public InterpersonalVO getByUserId(String userId){
    InterpersonalVO vo = InterpersonalVO.builder().build();
    if(StringUtils.isBlank(userId)){
      return vo;
    }
    InterpersonalDAO dao = interpersonalMapper.selectByUserId(userId);
    if(dao == null){
      return vo;
    }
    BeanUtils.copyProperties(dao,vo);
    return vo;
  }




  public List<String> removeList(List<String> oldList ,List<String> removeList){
    List<String> newList = new ArrayList<>(oldList);
    newList.removeAll(removeList);
    return newList;
  }







}

