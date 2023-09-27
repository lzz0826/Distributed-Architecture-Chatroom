package org.server.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Resource;
import org.server.cache.ChatSilenceCache;
import org.server.dao.ChatSilenceCacheDAO;
import org.server.vo.ChatSilenceCacheVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ChatSilenceCacheService {

  @Resource
  private ChatSilenceCache chatSilenceCache;



  public void addChatSilenceCacheByUserId(String userId ,String chatroomId , Integer timeout){
    chatSilenceCache.putByUserId(userId,chatroomId,timeout);
  }

  public Map<String, ChatSilenceCacheDAO> getSilenceCacheByUserId(String userId){
    return chatSilenceCache.getByUserId(userId);
  }


  public Map<String, ChatSilenceCacheDAO> getSilenceNowCacheByUserId(String userId){

    Map<String, ChatSilenceCacheDAO> repMap = new HashMap<>();

    Map<String, ChatSilenceCacheDAO> silenceCacheByUserId = getSilenceCacheByUserId(userId);
    silenceCacheByUserId.forEach((k,v) ->{
      long timeout = v.getTimeout();
      long currentTimeMillis = System.currentTimeMillis();
      if(timeout > currentTimeMillis){
        repMap.put(k,v);
      }
    });
    return repMap;
  }

  public Map<String, ChatSilenceCacheVO> ChatSilenceCacheDaoToVo(String userId){

    Map<String, ChatSilenceCacheVO> vo  = new HashMap<>();
    Map<String, ChatSilenceCacheDAO> dao = getSilenceNowCacheByUserId(userId);
    if (dao != null) {
      dao.forEach((key, value) -> {
        ChatSilenceCacheVO cacheVO = ChatSilenceCacheVO.builder().build();
        BeanUtils.copyProperties(value, cacheVO);
        vo.put(key, cacheVO);
      });
    }
    return vo;
  }


  public void delSilenceCacheByUserId(String userId , String chatroomId){
    chatSilenceCache.delChatRoomByUserId(userId,chatroomId);
  }

  public void delSilenceCacheByUserIdAll(String userId){
    chatSilenceCache.delByUserId(userId);
  }


}
