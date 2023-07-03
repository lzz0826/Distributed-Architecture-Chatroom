package org.server.service;

import javax.annotation.Resource;
import org.server.cache.ChatSilenceCache;
import org.springframework.stereotype.Service;

@Service
public class ChatSilenceCacheService {

  @Resource
  private ChatSilenceCache chatSilenceCache;




  public void addChatSilenceCacheByUserId(String userId ,String chatroomId , Integer timeout){
    chatSilenceCache.putByUserId(userId,chatroomId,timeout);
  }

  public String getSilenceCacheByUserId(String userId){
    return chatSilenceCache.getByUserId(userId);
  }

  public void delSilenceCacheByUserId(String userId){
    chatSilenceCache.delByUserId(userId);
  }


}
