package org.server.service;


import java.util.List;
import javax.annotation.Resource;
import org.server.cache.JwtTokenUserCache;
import org.server.cache.UserIdJwtTokenCache;
import org.server.pojo.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JwtCacheService {

  @Resource
  private JwtTokenUserCache jwtTokenUserCache;

  @Resource
  private UserIdJwtTokenCache userIdJwtTokenCache;


  @Resource
  private UserService userService;



  public void newToken(String newJwtToken, User user) {
    String userId = user.getId();
    String oldJwtToken = userIdJwtTokenCache.getByUserId(userId);
    jwtTokenUserCache.delByJwtToken(oldJwtToken);
    userIdJwtTokenCache.delByUserId(userId);
    jwtTokenUserCache.putByJwtToken(newJwtToken, user);
    userIdJwtTokenCache.putByUserId(userId, newJwtToken);
  }


  public void deleteTokenByUserId(String userId) {
    String jwtToken = userIdJwtTokenCache.getByUserId(userId);
    deleteTokenByJwtToken(jwtToken);
  }


  public void deleteTokenByJwtToken(String jwtToken){
    User user = jwtTokenUserCache.getByJwtToken(jwtToken);
    if(user != null){
      String userId = user.getId();
      userIdJwtTokenCache.delByUserId(userId);
    }
    jwtTokenUserCache.delByJwtToken(jwtToken);
  }


  public User getUserByJwtToken(String jwtToken) {
    return jwtTokenUserCache.getByJwtToken(jwtToken);
  }

  @Async("asyncServiceExecutor")
  public void deleteTokenByUsers(List<String> userIds){
    for(String userId : userIds){
      deleteTokenByUserId(userId);
    }
  }
















}
