package org.server.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.dao.BankCodeDAO;

@Mapper
public interface BankCodeMapper {

  int insertOrder(BankCodeDAO bankCodeDAO);

  List<BankCodeDAO> selectAll();

  BankCodeDAO selectById(@Param("id") String id);
  int updateById(BankCodeDAO bankCodeDAO);



}
