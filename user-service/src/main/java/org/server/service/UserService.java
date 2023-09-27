package org.server.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.server.dao.ChatroomDAO;
import org.server.entity.CustomUserDetails;
import org.server.exception.AddErrorException;
import org.server.exception.LoginErrorException;
import org.server.mapper.UserMapper;
import org.server.dao.UserDAO;
import org.server.sercice.IdGeneratorService;
import org.server.vo.ChatroomRecordVO;
import org.server.vo.ChatroomVO;
import org.server.vo.LoginVO;
import org.server.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService{


  @Resource
  private JwtCacheService jwtCacheService;

  @Resource
  private IdGeneratorService idGeneratorService;


  @Resource
  private UserMapper userMapper;

  @Resource
  private JwtService jwtService;

  @Resource
  private CustomUserDetailsService customUserDetailsService;


  public void addUser(String username , String password ,String address)
      throws AddErrorException {

    String md5token = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

    UserDAO user = UserDAO
        .builder()
        .id(idGeneratorService.getNextId())
        .username(username)
        .password(md5token)
        .address(address)
//        TODO 角色
        .role("ROLE_ADMIN")
        .updateTime(new Date())
        .createTime(new Date())
        .build();


    int i = userMapper.insertUser(user);

    if(i == 0){
      throw new AddErrorException();
    }

  }

  public int updateUser(UserDAO dao){
    return userMapper.updateUser(dao);
  }


  public UserDAO getUserByUsername(String username){
    return userMapper.selectByUsername(username);
  }


  public UserDAO getUserById(String id){
    return userMapper.selectById(id);
  }

  public List<UserDAO> getAllUsers(){
    return userMapper.selectAllUsers();
  }

  public Page<UserVO> getAllUserVOs(Integer page ,Integer pageSize){
    Page<UserVO> vos = new Page<>();
    Page<UserDAO> daos = PageHelper.startPage(page,pageSize)
        .doSelectPage(() -> userMapper.selectAllUsers());

    if(daos.isEmpty()){
      return vos;
    }
    BeanUtils.copyProperties(daos,vos);
    for (UserDAO dao : daos) {
      UserVO vo = convertDAOToVO(dao);
      vos.add(vo);
    }
    return vos;
  }

  public UserVO getUserVO(String id){
    UserDAO user = getUserById(id);
    if(user == null){
      return null;
    }
    return UserVO
        .builder()
        .id(user.getId())
        .username(user.getUsername())
        .avatarPth(user.getAvatarPth())
        .role(user.getRole())
        .address(user.getAddress())
        .createTime(user.getCreateTime())
        .updateTime(user.getUpdateTime())
        .build();

  }


  public LoginVO login(String username , String password) throws LoginErrorException {

    String md5token = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);
    if(!md5token.equals(customUserDetails.getPassword())){
      throw new LoginErrorException();
    }

    String jwtToken = jwtService.generateToken(customUserDetails);

    jwtCacheService.newToken(jwtToken,customUserDetails);

    return LoginVO
        .builder()
        .jwtToken(jwtToken)
        .userId(customUserDetails.getId())
        .build();


  }

  public UserVO convertDAOToVO(UserDAO dao){
    if(dao == null){
      return null;
    }
    UserVO vo = UserVO.builder()
        .id(dao.getId())
        .username(dao.getUsername())
        .avatarPth(dao.getAvatarPth())
        .address(dao.getAddress())
        .role(dao.getRole())
        .updateTime(dao.getUpdateTime())
        .createTime(dao.getCreateTime())
        .build();
    return vo;
  }









}
