package org.server.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.server.dao.BlackListDAO;

@Mapper
public interface BlackListMapper {

  int insertBlackList(BlackListDAO dao);

  int insertBlackLists(List<BlackListDAO> daos);


  BlackListDAO selectById(String id);

  BlackListDAO selectByUserId(String userId);

  int update(BlackListDAO dao);

  int deleteByIds(List<String> ids);

  int deleteByUserIds(List<String> userIds);










}