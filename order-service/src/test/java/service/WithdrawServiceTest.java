package service;

import base.BaseTest;
import java.math.BigDecimal;
import javax.annotation.Resource;
import org.junit.Test;
import org.server.exception.withdraw.MerchantNotFoundException;
import org.server.exception.withdraw.SignVerificationFailedException;
import org.server.withdraw.dto.TraderResponseDto;
import org.server.withdraw.req.CreateWithdrawRequest;
import org.server.withdraw.sercive.WithdrawService;

public class WithdrawServiceTest extends BaseTest {

  @Resource
  private WithdrawService withdrawService;

  @Test
  public void createWithdrawTest()
      throws NoSuchFieldException, IllegalAccessException, MerchantNotFoundException, SignVerificationFailedException {
    CreateWithdrawRequest req = CreateWithdrawRequest.builder()
        .userId("userId123")
        .payeeCardNo("yourPayeeCardNo")
        .payeeCardName("yourPayeeCardName")
        .branchName("yourBranchName")
        .bankProvince("yourBankProvince")
        .bankCity("yourBankCity")
        .accountId("yourAccountId")
        .accountName("yourAccountName")
        .bankCode("yourBankCode")
        .bankName("yourBankName")
        .targetAccountId("yourTargetAccountId")
        .targetAccountName("yourTargetAccountName")
        .targetBankCode("yourTargetBankCode")
        .targetBankName("yourTargetBankName")
        .clientIp("yourClientIp")
        .clientDevice("yourClientDevice")
        .orderNo("yourOrderNo")
        .amount(BigDecimal.valueOf(100.0)) // Set the amount value
        .currency("yourCurrency")
        .clientExtra("yourClientExtra")
        .notifyUrl("yourNotifyUrl")
        .sign("yourSign")
        .build();

    TraderResponseDto withdraw = withdrawService.createWithdraw(req, "123");
    System.out.println(withdraw);
  }

}
