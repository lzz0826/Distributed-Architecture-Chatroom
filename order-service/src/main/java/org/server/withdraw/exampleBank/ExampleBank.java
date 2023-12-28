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

    // 簽名規則  字段字典排序後拼接"&"符號字串
    // 簽名參數 假裝銀行方要是 出款帳戶id 出款帳戶名 和時間 +密鑰
    Map<String, Object> params = new LinkedHashMap<>();

    params.put("userId", withdrawChannel.getAccountId());
    params.put("userName", withdrawChannel.getAccountName());
    params.put("timestamp", FormatUtil.formatDateTime(new Date()));


    params.put("sign", makeSign(params, withdrawChannel.getBankSign()));
    log.info("{} 余额查询 params: {}", LOG_PREFIX, params);

    // 向第銀行發起查詢餘額请求
    String responseStr = OkHttpUtil.postFrom(withdrawChannel.getBalanceQueryUrl(), params);
    Map<String, Object> response = new HashMap<>();
    try{
      response = JSON.parseObject(responseStr, Map.class);
    }catch (Exception e){
      System.out.println("解析JSON responseStr失敗");
    }
//    Map<String, Object> response = FormDataUtil.post(withdrawChannel.getBalanceQueryUrl(), params, Map.class);
    log.info("{} 余额查询 response: {}", LOG_PREFIX, response);

    //模擬 成功碼的key = result  value = 0
    if (!RESPONSE_SUCCESS.equals(ConverterUtils.getAsString(response.get("result")))) {
      log.info("{} 餘額查詢失敗 reposne status code={} message={}", LOG_PREFIX, response.get("result"), response.get("msg"));
      throw new GetBalanceFailException();
    }

    //模擬 餘額key = money
    String balance = String.valueOf(response.get("money"));
    return ConverterUtils.getAsBigDecimal(balance, BigDecimal.ZERO);

  }

  @Override
  public WithdrawMethodResponse execute(WithdrawChannel withdrawChannel,
      WithdrawOrder withdrawOrder) throws Exception {
    return null;
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
    // 私鑰生成签名信息
    String signStr = SignUtil.makeSortSign(params, key);
    String sign = DigestUtils.md5Hex(signStr).toUpperCase();
    log.info("{} make sign, signStr={}, sign={}", LOG_PREFIX, signStr, sign);
    return sign;
  }
}
