package org.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.server.withdraw.model.WithdrawChannel;

@Mapper
public interface WithdrawBankChannelMapper {

  int insertWithdrawBankChannel(WithdrawChannel withdrawChannel);

  List<WithdrawChannel> findByMerchantIds(String merchantId);

  WithdrawChannel getWithdrawChannelDaoById(String id);

  WithdrawChannel getWithdrawChannelDaoByUserId(String userId);

  WithdrawChannel getWithdrawChannelDaoByMerchantId(String merchantId);








}
