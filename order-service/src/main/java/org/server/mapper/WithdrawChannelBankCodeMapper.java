package org.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.server.dao.WalletsDAO;
import org.server.withdraw.model.WithdrawChannelBankCode;

@Mapper
public interface WithdrawChannelBankCodeMapper {

  int insertWithdrawChannelBankCode(WithdrawChannelBankCode withdrawChannelBankCode);

  List<WithdrawChannelBankCode> findByBankName(String bankName);

  List<WithdrawChannelBankCode> findByBankCode(String bankCode);

  List<WithdrawChannelBankCode> findByIBankCodeIds(List<String> bankCodeIds);





}
