package service;

import base.BaseTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.withdraw.model.WithdrawChannelBankCode;
import org.server.withdraw.model.WithdrawChannelBankDetail;
import org.server.withdraw.sercive.JoinService;

public class JoinServiceTest extends BaseTest {

  @Resource
  private JoinService joinService;

  @Test
  public void getWithdrawChannelBankCodeMapTest(){

    List<String> list = new ArrayList<>();
    list.add("9993");
    list.add("9995");

    Map<String, WithdrawChannelBankCode> withdrawChannelBankCodeMap = joinService.getWithdrawChannelBankCodeMap(list);
    for (Entry<String, WithdrawChannelBankCode> entry : withdrawChannelBankCodeMap.entrySet()) {
      System.out.println(entry.getValue());
    }

    System.out.println("---------");
    System.out.println(withdrawChannelBankCodeMap.get("9993"));

  }

  @Test
  public void Test(){
    String withdrawBankChannelId = "0204";

    List<WithdrawChannelBankDetail> list = joinService.bankDetailJoinBankCode(withdrawBankChannelId);
    System.out.println("---------");

    System.out.println(list);
    System.out.println("---------");


    for (WithdrawChannelBankDetail channelBankDetail : list) {
      WithdrawChannelBankCode withdrawChannelBankCode = channelBankDetail.getWithdrawChannelBankCode();
      System.out.println(withdrawChannelBankCode);
    }

  }


}
