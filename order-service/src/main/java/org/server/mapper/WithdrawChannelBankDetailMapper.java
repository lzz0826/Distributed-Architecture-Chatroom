package org.server.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.withdraw.model.WithdrawChannelBankDetail;

@Mapper
public interface WithdrawChannelBankDetailMapper {

  int insertWithdrawChannelBankDetail(WithdrawChannelBankDetail withdrawChannelBankDetail);

  WithdrawChannelBankDetail findWithdrawChannelBankDetailById(@Param("withdrawChannelBankDetailId") String withdrawChannelBankDetailId);


  List<WithdrawChannelBankDetail> findByWithdrawBankChannelId(@Param("withdrawBankChannelId") String withdrawBankChannelId);

}
