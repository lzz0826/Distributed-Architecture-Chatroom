package org.server.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.dao.WalletsDAO;

@Mapper
public interface WalletsMapper {


  int insertWallets(WalletsDAO walletsDAO);

  List<WalletsDAO> selectAll();

  WalletsDAO selectById(@Param("walletId") String walletId);

  WalletsDAO selectByIdUserID(@Param("userId") String userId);


  int updateByUserId(WalletsDAO dao);

  int increaseBalanceByWalletId(@Param("walletId") String walletId ,
      @Param("increase") BigDecimal Increase ,@Param("updateTime")Date updateTime );


  int reduceBalanceByWalletId(@Param("walletId") String walletId ,
      @Param("reduce") BigDecimal reduce ,@Param("updateTime")Date updateTime );



}
