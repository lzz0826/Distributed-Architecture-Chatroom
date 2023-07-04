package org.server.service;

import static org.server.util.StringUtil.strListToString;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.server.dao.InterpersonalDAO;
import org.server.mapper.InterpersonalMapper;
import org.server.sercice.IdGeneratorService;
import org.springframework.stereotype.Service;

@Service
public class InterpersonalService {

  @Resource
  private InterpersonalMapper interpersonalMapper;

  @Resource
  private IdGeneratorService idGeneratorService;

  public void addInsertInterpersonal(String userId, List<String> blacklist,
      List<String> blacklisted, List<String> banChatRoom) {

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

  }




}
