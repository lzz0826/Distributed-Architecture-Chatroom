package org.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.dao.BankcardAccountDAO;

@Mapper
public interface BankAccountMapper {

  int insertBankAccount(BankcardAccountDAO bankcardAccountDAO);

  List<BankcardAccountDAO> selectAll();

  BankcardAccountDAO selectByBankCardAccountId(@Param("id") String id);

  int updateByBankcardAccount(BankcardAccountDAO bankcardAccountDAO);




}
