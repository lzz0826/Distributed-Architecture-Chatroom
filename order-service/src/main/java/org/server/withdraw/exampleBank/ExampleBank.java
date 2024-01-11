package org.server.withdraw.exampleBank;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.server.exception.bank.GetBalanceRequestFailException;
import org.server.exception.bank.InquireWithdrawOrderRequestFailException;
import org.server.exception.bank.WithdrawCallbackSignFailException;
import org.server.exception.bank.WithdrawOrderNotExistException;
import org.server.exception.bank.WithdrawRequestFailException;
import org.server.utils.ConverterUtils;
import org.server.utils.FormatUtil;
import org.server.utils.OkHttpUtil;
import org.server.utils.SignUtil;
import org.server.withdraw.enums.HttpRequestEnum;
import org.server.withdraw.model.TraderResponseCode;
import org.server.withdraw.model.WithdrawBank;
import org.server.withdraw.rep.WithdrawMethodResponse;
import org.server.withdraw.rep.WithdrawNotifyResponse;
import org.server.withdraw.model.WithdrawOrder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ExampleBank implements org.server.withdraw.WithdrawBank {

  private static final String LOG_PREFIX = "ExampleBank出款";

  //銀行回應(文件)  final
  private static final String RESPONSE_SUCCESS = "0";

  private static final String RESPONSE_MESSAGE = "msg";

  private static final String RESPONSE_MESSAGE_WITHDRAW_ORDER_NOT_EXIST = "order not exist";

  private static final String RESPONSE_CODE = "code";

  private static final String RESPONSE_STATUS = "status";

  private static final String RESPONSE_DATA = "data";

  //回調的orderId參數key
  private static final String CALLBACK_ORDER_ID = "orderNO";


  @Resource
  private ObjectMapper objectMapper;




  @Override
  public boolean canSupport(String catchId) {
    return "EXAMPLEBANK".equals(catchId);
  }

  @Override
  public boolean canDoQueryBalance() {
    // 支持餘額查詢
    return true;
  }

  @Override
  public BigDecimal getBalance(WithdrawBank withdrawBank) throws Exception {

    //須核對銀行文件
    // 簽名規則  字段字典排序後拼接"&"符號字串 FROM表單
    // 簽名參數 假裝銀行方要是 出款帳戶id 出款帳戶名 和時間 +密鑰
    Map<String, Object> params = new TreeMap<>();

    params.put("accountId", withdrawBank.getAccountId());
    params.put("accountName", withdrawBank.getAccountName());
    params.put("timestamp", FormatUtil.formatDateTime(new Date()));

    params.put("sign", makeSign(params, withdrawBank.getBankSign()));
    log.info("{} 餘額查詢 params: {}", LOG_PREFIX, params);

    // 向第銀行發起查詢餘額請求
    String responseStr = OkHttpUtil.postFrom(withdrawBank.getBalanceQueryUrl(), params);
    Map<String, Object> response = new HashMap<>();
    try {
      response = JSON.parseObject(responseStr, Map.class);
    } catch (Exception e) {
      System.out.println("解析JSON responseStr失敗");
    }
//    Map<String, Object> response = FormDataUtil.post(withdrawChannel.getBalanceQueryUrl(), params, Map.class);
    log.info("{} 餘額查詢 response: {}", LOG_PREFIX, response);

    //模擬 成功碼的key = code  value = 0
    String resultCode = ConverterUtils.getAsString(response.get(RESPONSE_CODE));

    if (!RESPONSE_SUCCESS.equals(resultCode)) {
      ////模擬 返回訊息key為 msg
      String msg = String.valueOf(response.get(RESPONSE_MESSAGE));
      log.info("{} 餘額查詢失敗 reposne status code={} message={}", LOG_PREFIX, resultCode, msg);
      throw new GetBalanceRequestFailException();
    }

//    data : {
//      balance : 100,
//    }

    //模擬 餘額key = balance
    Map<String, Object> data = (Map<String, Object>) response.get(RESPONSE_DATA);
    String balance = String.valueOf(data.get("balance"));
    return ConverterUtils.getAsBigDecimal(balance, BigDecimal.ZERO);
  }


  @Override
  public WithdrawMethodResponse execute(WithdrawBank withdrawBank,
      WithdrawOrder withdrawOrder) throws Exception {

//    String notifyUrl = withdrawVendorMapper.findOneByCatchId(withdrawChannel.getCatchId()).getOurNotifyUrl();


    //須核對銀行文件
    // 簽名規則  字段字典排序後拼接"&"符號字串 FROM表單
    // 簽名參數 假裝銀行方要是 出款帳戶id 出款帳戶名 目標帳戶id 目標帳戶名 和時間 +密鑰

    Map<String, Object> params = new TreeMap<>();
    params.put("accountId", withdrawBank.getAccountId());
    params.put("accountName", withdrawBank.getAccountName());
    params.put("targetAccountName", withdrawBank.getTargetAccountId());
    params.put("targetAccountName", withdrawBank.getTargetBankName());
    params.put("orderAmount", withdrawOrder.getAmount());
    params.put("payType", 1);//付款方式 固定值:1
    params.put("accountHolderName", withdrawOrder.getPayeeCardName());//收款人姓名
    params.put("accountNumber", withdrawOrder.getPayeeCardNo());//收款人銀行卡號
    params.put("bankType", withdrawBank.getBankCode());//銀行編號
    params.put("notifyUrl", withdrawOrder.getNotifyUrl()); //回調url
    params.put("timestamp", FormatUtil.formatDateTime(new Date()));
    //簽名
    params.put("sign", makeSign(params, withdrawBank.getBankSign()));
    //不參與簽名的參數
    params.put("sign", makeSign(params, withdrawOrder.getPayeeCardNo()));

    log.info("[{}] {}下單 request: {}", withdrawOrder.getWithdrawOrderId(), LOG_PREFIX, params);
    String orderOriginationUrl = withdrawBank.getOrderOriginationUrl();
    // 向第銀行出款請求
    String responseStr = OkHttpUtil.postFrom(orderOriginationUrl, params);
    Map<String, Object> response = new HashMap<>();
    try {
      response = JSON.parseObject(responseStr, Map.class);
    } catch (Exception e) {
      System.out.println("解析JSON responseStr失敗");
    }
    log.info("{}下單結果={}", LOG_PREFIX, response);

    //模擬 成功碼的key = result  value = 0
    //ConverterUtils.getAsString 會返回null指針 String.valueOf會返回"null"
    String resultCode = ConverterUtils.getAsString(response.get(RESPONSE_CODE));
    String msg = String.valueOf(response.get(RESPONSE_MESSAGE));

    if (!RESPONSE_SUCCESS.equals(resultCode)) {
      log.info("{} 出款請求失敗 reposne status code={} message={}", LOG_PREFIX, resultCode, msg);
      throw new WithdrawRequestFailException();
    }

    return WithdrawMethodResponse.builder()
        .returnCode(TraderResponseCode.SUCCESS.getCode())
        .returnMessage(msg)
        .withdrawOrderId(withdrawOrder.getWithdrawOrderId())
        .amount(withdrawOrder.getAmount())
        .orderStatus(withdrawOrder.getStatus())
//        .orderMsg()
        .build();
  }

  @Override
  public WithdrawMethodResponse getOrder(WithdrawOrder withdrawOrder,
      WithdrawBank withdrawBank, HttpRequestEnum enums) throws Exception {

    //須核對銀行文件
    // 簽名規則  字段字典排序後拼接"&"符號字串 FROM表單
    // 簽名參數 假裝銀行方要是 出款帳戶id 訂單號 目標帳戶名 和時間 +密鑰
    Map<String, Object> params = new TreeMap<>();
    params.put("accountId", withdrawBank.getAccountId());
    params.put("orderNo", withdrawOrder.getWithdrawOrderId());
    params.put("reqTime", FormatUtil.formatDateTimeCompact(new Date()));//請求發起時間,時間格式:yyyyMMddHHmmss
    params.put("sign", makeSign(params, withdrawBank.getBankSign()));
    log.info("{}查詢訂單={}", LOG_PREFIX, params);

    String orderQueryUrl = withdrawBank.getOrderQueryUrl();
    // 向第三方代付發起請求
    String responseStr = OkHttpUtil.postFrom(orderQueryUrl, params);
    Map<String, Object> response = new HashMap<>();
    try {
      response = JSON.parseObject(responseStr, Map.class);
    } catch (Exception e) {
      System.out.println("解析JSON responseStr失敗");
    }
    log.info("{}下單結果={}", LOG_PREFIX, response);
    log.info("{}取款查詢訂單結果 response: {}", LOG_PREFIX, response);

    // 判斷回應碼
    String resultCode = String.valueOf(response.get(RESPONSE_CODE));
    if (!RESPONSE_SUCCESS.equals(resultCode)) {
      //依照銀行 文件 :0-待處理,  1-處理中,  2-成功,  3-失敗
      String responseMessage = String.valueOf(response.get(RESPONSE_MESSAGE));

      // 找出訂單不存在的msg做處理 (需要核對銀行方文件的返回)
      if (RESPONSE_MESSAGE_WITHDRAW_ORDER_NOT_EXIST.equals(responseMessage)) {
        throw new WithdrawOrderNotExistException();
      } else {
        log.info("{}取款訂單查詢失敗 reposne status code={} message={}", LOG_PREFIX, resultCode,
            responseMessage);
        throw new InquireWithdrawOrderRequestFailException();
      }
    }

    //依照銀行 文件 :0-待處理,  1-處理中,  2-成功,  3-失敗
    String status = String.valueOf(response.get(RESPONSE_STATUS));
    if (ResponseCode.SUCCESS.getCode().equals(status)) {
      status = ResponseCode.SUCCESS.getCode();
    }
    // 成功：succ 失敗：fail 處理中：handling
    ResponseCode responseCode = ResponseCode.getResponse(status);

    return WithdrawMethodResponse.builder()
        .returnCode(status)
        .returnMessage(responseCode.getDesc())
        .withdrawOrderId(withdrawOrder.getWithdrawOrderId())
        .orderStatus(responseCode.getStatus())
        .build();

  }

  @Override
  public WithdrawNotifyResponse checkNotifySign(WithdrawBank withdrawBank,
      Map<String, String> params) throws Exception {

    Map<String, Object> map = objectMapper.convertValue(params,Map.class);
    if (!verifySign(map, withdrawBank.getBankSign())) {
      log.info("{}取款訂單回調驗簽失敗 params :{}", LOG_PREFIX, params);
      throw new WithdrawCallbackSignFailException();
    }
    return WithdrawNotifyResponse.builder()
        //withdrawOrderId 以回調來的 order key 為準
        .withdrawOrderId(params.get(CALLBACK_ORDER_ID))
        .amount(withdrawBank.getTotalAmount())
        .status(withdrawBank.getStatus())
        .build();

  }

  @Override
  public boolean shouldChargeFeeOnFailed() {
    // 不使用
    return false;
  }

  @Override
  public String getThirdOrderSuccessCode() {
    return ResponseCode.SUCCESS.getCode();
  }

  private static String makeSign(Map<String, Object> params, String key) {
    // 私鑰生成簽名信息
    String signStr = SignUtil.makeSortSign(params, key);
    String sign = DigestUtils.md5Hex(signStr).toUpperCase();
    log.info("{} make sign, signStr={}, sign={}", LOG_PREFIX, signStr, sign);
    return sign;
  }

  private boolean verifySign(Map<String, Object> map, String key) {
    String encSign = (String) map.get("sign");

    // remove不參與驗簽的參數
    map.remove("sign");
    Map<String, Object> signMap= new HashMap<>();

    map.forEach((k,v)->{
      if(StringUtils.isNotBlank(String.valueOf(v))){
        signMap.put(k,v);
      }
    });
    return encSign.equals(makeSign(signMap, key));
  }

  @Getter
  @AllArgsConstructor
  private enum ResponseCode {
    //依照銀行 文件 :0-待處理,  1-處理中,  2-成功,  3-失敗
    SUCCESS("2", "成功", WithdrawOrder.STATUS_WITHDRAWN),
    REJECT_NOTIFY("3", "失敗", WithdrawOrder.STATUS_FAILED_ON_WITHDRAWING),
    READY_WITHDRAWING("0", "處理中", WithdrawOrder.STATUS_WITHDRAWING),
    WITHDRAWING("1", "處理中", WithdrawOrder.STATUS_WITHDRAWING),
    SYSTEM_ERR("xxx", "金流商回傳未知訂單狀態", WithdrawOrder.STATUS_WITHDRAWING);

    private String code;
    private String desc;
    // 對應我們定義的狀態
    private int status;

    public static ResponseCode getResponse(String code) {
      return Stream.of(ResponseCode.values()).filter(r -> r.getCode().equals(code)).findFirst()
          .orElse(SYSTEM_ERR);
    }
  }
}
