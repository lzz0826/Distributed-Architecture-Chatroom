package org.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.server.withdraw.model.WithdrawOrder;
import org.springframework.data.repository.query.Param;

@Mapper
public interface WithdrawOrderMapper {

  int insertWithdrawOrder(WithdrawOrder withdrawOrder);

  //withdrawMinute 分鐘內的
  Integer findVerifyPayeeCardNoCount(WithdrawOrder withdrawOrder);

  List<WithdrawOrder> selectAll();










}
