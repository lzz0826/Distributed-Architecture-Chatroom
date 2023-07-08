package org.server.service;

import static org.server.util.StringUtil.strListToString;
import static org.server.util.StringUtil.strSetToString;
import static org.server.util.StringUtil.stringToStrList;
import static org.server.util.StringUtil.stringToStrSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.server.dao.InterpersonalDAO;
import org.server.exception.Interpersonal.AddInterpersonalFailException;
import org.server.exception.Interpersonal.EditInterpersonalException;
import org.server.exception.NotFoundUserException;
import org.server.mapper.InterpersonalMapper;
import org.server.sercice.IdGeneratorService;
import org.server.vo.InterpersonalVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class InterpersonalService {

  @Resource
  private InterpersonalMapper interpersonalMapper;

  @Resource
  private IdGeneratorService idGeneratorService;

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

  public InterpersonalVO editAddInterpersonal(String userId, List<String> blacklist,
      List<String> blacklisted, List<String> banChatRoom)
      throws AddInterpersonalFailException, EditInterpersonalException {

    InterpersonalVO vo = InterpersonalVO.builder().build();

    InterpersonalDAO dao = interpersonalMapper.selectByUserId(userId);

    if(dao == null){
      vo = addInsertInterpersonal(userId, blacklist, blacklisted, banChatRoom);
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
    }
    return vo;
  }


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
    }
    return vo;
  }


  public List<String> removeList(List<String> oldList ,List<String> removeList){
    List<String> newList = new ArrayList<>(oldList);
    newList.removeAll(removeList);
    return newList;
  }

}

