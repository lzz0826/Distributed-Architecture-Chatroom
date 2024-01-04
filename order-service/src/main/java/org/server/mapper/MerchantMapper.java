package org.server.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.server.withdraw.model.Merchant;

@Mapper
public interface MerchantMapper {

  int insertMerchant(Merchant merchant);

  Merchant selectByUserId(@Param("userId") String userId);






}
