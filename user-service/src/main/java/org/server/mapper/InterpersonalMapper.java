package org.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.server.dao.InterpersonalDAO;

@Mapper
public interface InterpersonalMapper {

  int insertInterpersonal(InterpersonalDAO dao);

  InterpersonalDAO selectById(String id);

  InterpersonalDAO selectByUserId(String userId);

  int update(InterpersonalDAO dao);






}
