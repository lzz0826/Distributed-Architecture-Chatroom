package org.server.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.withdraw.model.WithdrawChannelBank;

@Mapper
public interface WithdrawChannelBankMapper {

  int insertWithdrawChannelBank(WithdrawChannelBank withdrawChannelBank);

  WithdrawChannelBank findWithdrawChannelBankById(@Param("withdrawChannelBankId") String withdrawChannelBankId);


  List<WithdrawChannelBank> findByWithdrawBankChannelId(@Param("withdrawBankChannelId") String withdrawBankChannelId);

}
