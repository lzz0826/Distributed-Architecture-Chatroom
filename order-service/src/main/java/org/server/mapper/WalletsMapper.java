package org.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.dao.OrderDAO;
import org.server.dao.WalletsDAO;

@Mapper
public interface WalletsMapper {


  int insertWallets(WalletsDAO walletsDAO);

  List<WalletsDAO> selectAll();

  WalletsDAO selectById(@Param("walletId") String walletId);



}
