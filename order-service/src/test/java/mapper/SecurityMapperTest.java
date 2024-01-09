package mapper;

import base.BaseTest;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.mapper.SecurityMapper;
import org.server.withdraw.model.Security;

public class SecurityMapperTest extends BaseTest {


  @Resource
  SecurityMapper securityMapper;


  @Test
  public void insertSecurityTest(){

    Security security = Security
        .builder()
        .securityId("4466")
        .type(Security.TYPE_WITHDRAW_CARD_NO)
        .status(Security.STATUS_ENABLED)
        .securityKey("gges")
        .securityValue("value")
        .memo("備註")
        .createTime(new Date())
        .updateTime(new Date())
        .build();


    int i = securityMapper.insertSecurity(security);
    System.out.println(i);
  }

  @Test
  public void findSecurityTest(){
    Security build = Security
        .builder()
        .securityId("4466")
        .type(Security.TYPE_WITHDRAW_CARD_NO)
        .status(Security.STATUS_ENABLED)
        .securityKey("gges")
        .build();

    List<Security> security = securityMapper.findSecurity(build);
    System.out.println(security);
  }

}
