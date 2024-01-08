package org.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.dao.BankcardAccountDAO;
import org.server.withdraw.model.WithdrawBankCardBlack;

@Mapper
public interface WithdrawBankCardBlackMapper {

  int insertWithdrawBankCardBlack(WithdrawBankCardBlack withdrawBankCardBlack);

  List<WithdrawBankCardBlack> findByMerchantIdBankCardNo(String merchantId , String bankCardNo);

}


