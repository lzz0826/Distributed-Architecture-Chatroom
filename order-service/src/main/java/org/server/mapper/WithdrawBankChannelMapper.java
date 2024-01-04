package org.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.server.dao.WithdrawChannelDao;

@Mapper
public interface WithdrawBankChannelMapper {

  int insertWithdrawBankChannel(WithdrawChannelDao withdrawChannelDao);


  WithdrawChannelDao getWithdrawChannelDaoById(String id);

  WithdrawChannelDao getWithdrawChannelDaoByUserId(String userId);

  WithdrawChannelDao getWithdrawChannelDaoByMerchantId(String merchantId);






}
