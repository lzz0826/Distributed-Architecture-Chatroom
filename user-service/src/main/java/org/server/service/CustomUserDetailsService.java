package org.server.service;


import java.util.ArrayList;
import javax.annotation.Resource;
import javax.security.auth.login.LoginException;
import org.server.dao.UserDAO;
import org.server.entity.CustomUserDetails;
import org.server.exception.LoginErrorException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Resource
  private UserService userService;

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserDAO userDAO = userService.getUserByUsername(username);

    if (userDAO == null) {
      try {
        throw new LoginErrorException();
      } catch (LoginErrorException e) {
        throw new RuntimeException(e);
      }
    }

    String password = userDAO.getPassword();

    String role = userDAO.getRole();

    // 设置 权限,可以是从数据库中查找出来的
    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(role));

    return CustomUserDetails.builder()
        .id(userDAO.getId())
        .username(userDAO.getUsername())
        .password(password)
        .authorities(authorities) // 设置权限和角色
        .enabled(true)
        .accountNonExpired(true)
        .accountNonLocked(true)
        .credentialsNonExpired(true)
        .build();

  }
}
