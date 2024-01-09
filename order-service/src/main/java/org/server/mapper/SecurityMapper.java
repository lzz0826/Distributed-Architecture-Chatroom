package org.server.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.server.withdraw.model.Security;

@Mapper
public interface SecurityMapper {

  int insertSecurity(Security security);


  List<Security> findSecurity(Security security);





}
