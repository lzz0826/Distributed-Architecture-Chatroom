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

  WithdrawOrder selectByWithdrawOrderId(String withdrawOrderId);

  List<WithdrawOrder> selectAll();

  int updateWithdrawOrderStatus(WithdrawOrder withdrawOrder);

  int updateWithdrawOrder(WithdrawOrder withdrawOrder);

  List<WithdrawOrder> selectByMerchantIdAndBankOrderNo(@Param("merchantId") String merchantId ,
      @Param("bankOrderNo") String bankOrderNo);


  /**
   * 根據狀態和指定天數前的時間，查詢提現訂單
   *
   * @param status 提現訂單狀態
   * @param day    查詢幾天前的數據
   * @return 符合條件的提現訂單
   */
  List<WithdrawOrder> selectByStatusAndDay(@Param("status")int status ,@Param("day") int day);












}
