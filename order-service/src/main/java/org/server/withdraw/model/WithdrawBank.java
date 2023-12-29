package org.server.withdraw.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawBank {
    public static final String CHANNEL_CODE = "WITHDRAW";
    public static final String CATCH_ID_TEST = "TEST";
    public static final String CATCH_ID_TEST_FAILE = "TEST_FAIL";
    public static final String CATCH_ID_TIME_OUT = "TEST_TIME_OUT";
    public static final String CATCH_ID_TONG_PAY = "TONGPAY";
    public static final String CATCH_ID_WPAY = "WPAY";

    public static final int STATUS_DISABLED = 0; // 下架
    public static final int STATUS_ENABLED = 1; // 啟用
    public static final int STATUS_REACHED_LIMIT = 2; // 達標
    public static final int STATUS_UNDER_RISK_CONTROL = 3; // 風控
    public static final int STATUS_SUSPENDED = 4; // 暫時禁用
    public static final int STATUS_COOLING = 5; // 冷卻
    public static final int STATUS_AMOUNT_COOLING = 6; // 金額冷卻
    
    public static final int SERVICEFEETYPE_BALANCE = 0; // 從餘額扣除手續費
    public static final int SERVICEFEETYPE_AMOUNT = 1; // 從出款金額扣除手續費

    //----我方轉出款相關
    /**
     * 出款帳號ID(我方)
     */
    private Long accountId;
    /**
     * 出款人名稱(我方)
     */
    private String accountName;
    /**
     * 出款銀行代碼(我方)
     */
    private String bankCode;

    /**
     * 出款銀行名稱(我方)
     */
    private String bankName;


    //----轉入方相關
    /**
     * 收款帳號ID(對方)
     */
    private Long targetAccountId;
    /**
     * 收款人名稱(對方)
     */
    private String targetAccountName;
    /**
     * 收款銀行代碼(對方)
     */
    private String targetBankCode;
    /**
     * 收款銀行名稱(對方)
     */
    private String targetBankName;

    /**
     * 狀態
     */
    private Integer status;

    /**
     * 銀行方額外參數，JSON格式
     */
    private String channelExtra;
    /**
     * 出款公鑰
     */
    private String publicKey;
    /**
     * 出款私鑰
     */
    private String privateKey;

    /**
     * 銀行方密钥
     */
    private String bankSign;

    /**
     * 登入帳號
     */
    private String loginUsername;
    /**
     * 登入密碼
     */
    private String loginPassword;
    /**

    /**
     * 出款金額
     */
    private BigDecimal totalAmount;

    /**
     * 備註
     */
    private String memo;

    /**
     * 帳號餘額，單位分
     */
    private BigDecimal balance;

    /**
     * 創建時間
     */
    private Date createTime;
    /**
     * 更新時間
     */
    private Date updateTime;
    /**
     * 創建人
     */
    private String creator;
    /**
     * 更新人
     */
    private String updater;

    /**
     * 訂單發起URL
     */
    private String orderOriginationUrl;
    /**
     * 訂單查詢URL
     */
    private String orderQueryUrl;
    /**
     * 餘額查詢URL
     */
    private String balanceQueryUrl;






}
