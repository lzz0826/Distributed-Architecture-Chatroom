package org.server.service;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import org.server.cache.JwtTokenUserCache;
import org.server.cache.UserIdJwtTokenCache;
import org.server.entity.CustomUserDetails;
import org.springframework.scheduling.annotation.Async;

@Service
public class JwtCacheService {

  @Resource
  private JwtTokenUserCache jwtTokenUserCache;

  @Resource
  private UserIdJwtTokenCache userIdJwtTokenCache;





  public void newToken(String newJwtToken, CustomUserDetails customUserDetails) {
    String userId = customUserDetails.getId();
    String oldJwtToken = userIdJwtTokenCache.getByUserId(userId);
    jwtTokenUserCache.delByJwtToken(oldJwtToken);
    userIdJwtTokenCache.delByUserId(userId);
    jwtTokenUserCache.putByJwtToken(newJwtToken, customUserDetails);
    userIdJwtTokenCache.putByUserId(userId, newJwtToken);
  }


  public void deleteTokenByUserId(String userId) {
    String jwtToken = userIdJwtTokenCache.getByUserId(userId);
    deleteTokenByJwtToken(jwtToken);
  }


  public void deleteTokenByJwtToken(String jwtToken){
    CustomUserDetails user = jwtTokenUserCache.getByJwtToken(jwtToken);
    if(user != null){
      String userId = user.getId();
      userIdJwtTokenCache.delByUserId(userId);
    }
    jwtTokenUserCache.delByJwtToken(jwtToken);
  }


  public CustomUserDetails getUserByJwtToken(String jwtToken) {
    return jwtTokenUserCache.getByJwtToken(jwtToken);
  }

  @Async("asyncServiceExecutor")
  public void deleteTokenByUsers(List<String> userIds){
    for(String userId : userIds){
      deleteTokenByUserId(userId);
    }
  }
















}
