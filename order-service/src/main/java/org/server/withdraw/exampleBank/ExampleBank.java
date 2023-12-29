package org.server.withdraw.exampleBank;

import com.alibaba.fastjson2.JSON;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.server.common.BaseResp;
import org.server.exception.bank.GetBalanceFailException;
import org.server.utils.ConverterUtils;
import org.server.utils.FormDataUtil;
import org.server.utils.FormatUtil;
import org.server.utils.OkHttpUtil;
import org.server.utils.SignUtil;
import org.server.withdraw.WithdrawBank;
import org.server.withdraw.model.HttpRequestEnum;
import org.server.withdraw.model.TraderResponseCode;
import org.server.withdraw.model.WithdrawChannel;
import org.server.withdraw.model.WithdrawMethodResponse;
import org.server.withdraw.model.WithdrawNotifyResponse;
import org.server.withdraw.model.WithdrawOrder;

@Log4j2
public class ExampleBank implements WithdrawBank {

  private static final String LOG_PREFIX = "ExampleBank出款";

  private static final String RESPONSE_SUCCESS = "0";


  @Override
  public boolean canSupport(String bankId) {
    return false;
  }

  @Override
  public boolean canDoQueryBalance() {
    return false;
  }

  @Override
  public BigDecimal getBalance(WithdrawChannel withdrawChannel) throws Exception {

    // 簽名規則  字段字典排序後拼接"&"符號字串 FROM表單
    // 簽名參數 假裝銀行方要是 出款帳戶id 出款帳戶名 和時間 +密鑰
    Map<String, Object> params = new LinkedHashMap<>();

    params.put("accountId", withdrawChannel.getAccountId());
    params.put("accountName", withdrawChannel.getAccountName());
    params.put("timestamp", FormatUtil.formatDateTime(new Date()));


    params.put("sign", makeSign(params, withdrawChannel.getBankSign()));
    log.info("{} 餘額查詢 params: {}", LOG_PREFIX, params);

    // 向第銀行發起查詢餘額請求
    String responseStr = OkHttpUtil.postFrom(withdrawChannel.getBalanceQueryUrl(), params);
    Map<String, Object> response = new HashMap<>();
    try{
      response = JSON.parseObject(responseStr, Map.class);
    }catch (Exception e){
      System.out.println("解析JSON responseStr失敗");
    }
//    Map<String, Object> response = FormDataUtil.post(withdrawChannel.getBalanceQueryUrl(), params, Map.class);
    log.info("{} 餘額查詢 response: {}", LOG_PREFIX, response);

    //模擬 成功碼的key = code  value = 0
    String resultCode = ConverterUtils.getAsString(response.get("code"));

    if (!RESPONSE_SUCCESS.equals(resultCode)) {
      ////模擬 返回訊息key為 msg
      String msg = String.valueOf(response.get("msg"));
      log.info("{} 餘額查詢失敗 reposne status code={} message={}", LOG_PREFIX, resultCode,msg );
      throw new GetBalanceFailException();
    }

//    data : {
//      balance : 100,
//    }

    //模擬 餘額key = balance
    Map<String, Object> data = (Map<String, Object>) response.get("data");
    String balance = String.valueOf(data.get("balance"));
    return ConverterUtils.getAsBigDecimal(balance, BigDecimal.ZERO);
  }

  @Override
  public WithdrawMethodResponse execute(WithdrawChannel withdrawChannel,
      WithdrawOrder withdrawOrder) throws Exception {

    // 簽名規則  字段字典排序後拼接"&"符號字串 FROM表單
    // 簽名參數 假裝銀行方要是 出款帳戶id 出款帳戶名 目標帳戶id 目標帳戶名 和時間 +密鑰

    Map<String, Object> params = new LinkedHashMap<>();

    params.put("accountId", withdrawChannel.getAccountId());
    params.put("accountName", withdrawChannel.getAccountName());
    params.put("targetAccountName", withdrawChannel.getTargetAccountId());
    params.put("targetAccountName", withdrawChannel.getTargetBankName());
    params.put("orderAmount",  withdrawOrder.getAmount());
    params.put("payType", 1);//付款方式 固定值:1
    params.put("accountHolderName", withdrawOrder.getPayeeCardName());//收款人姓名
    params.put("accountNumber", withdrawOrder.getPayeeCardNo());//收款人银行卡号
    params.put("bankType",withdrawChannel.getBankCode());//银行编号
    params.put("notifyUrl", withdrawOrder.getNotifyUrl()); //回調url
    params.put("timestamp", FormatUtil.formatDateTime(new Date()));
    //簽名
    params.put("sign", makeSign(params, withdrawChannel.getBankSign()));
    //不參與簽名的參數
    params.put("sign", makeSign(params, withdrawOrder.getPayeeCardNo()));

    log.info("[{}] {}下单 request: {}", withdrawOrder.getWithdrawOrderId(), LOG_PREFIX, params);
    String orderOriginationUrl = withdrawChannel.getOrderOriginationUrl();
    // 向第銀行出款請求
    String responseStr = OkHttpUtil.postFrom(orderOriginationUrl,params);
    Map<String, Object> response = new HashMap<>();
    try{
      response = JSON.parseObject(responseStr, Map.class);
    }catch (Exception e){
      System.out.println("解析JSON responseStr失敗");
    }    log.info("{}下單結果={}", LOG_PREFIX, response);

    //模擬 成功碼的key = result  value = 0
    //ConverterUtils.getAsString 會返回null指針 String.valueOf會返回"null"
    String resultCode = ConverterUtils.getAsString(response.get("code"));
    String msg = String.valueOf(response.get("msg"));

    if (!RESPONSE_SUCCESS.equals(resultCode)) {
      log.info("{} 出款請求失敗 reposne status code={} message={}", LOG_PREFIX, resultCode, msg);
//      throw new GetBalanceFailException();
    }

    return WithdrawMethodResponse.builder()
        .returnCode(TraderResponseCode.SUCCESS.getCode())
        .returnMessage(msg)
        .withdrawOrderId(withdrawOrder.getWithdrawOrderId())
//        .amount()
//        .orderStatus()
//        .orderMsg()
        .build();
  }

  @Override
  public WithdrawMethodResponse getOrder(WithdrawOrder withdrawOrder,
      WithdrawChannel withdrawChannel, HttpRequestEnum enums) throws Exception {
    return null;
  }

  @Override
  public WithdrawNotifyResponse checkNotifySign(WithdrawChannel withdrawChannel,
      Map<String, String> params) throws Exception {
    return null;
  }

  @Override
  public boolean shouldChargeFeeOnFailed() {
    return false;
  }

  @Override
  public String getThirdOrderSuccessCode() {
    return null;
  }

  private static String makeSign(Map<String, Object> params, String key) {
    // 私鑰生成簽名信息
    String signStr = SignUtil.makeSortSign(params, key);
    String sign = DigestUtils.md5Hex(signStr).toUpperCase();
    log.info("{} make sign, signStr={}, sign={}", LOG_PREFIX, signStr, sign);
    return sign;
  }
}
