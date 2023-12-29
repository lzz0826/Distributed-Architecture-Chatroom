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
public class WithdrawOrder {
    public final static int STATUS_FAILED_ON_SIGN = -4; // 驗證失敗
    public final static int STATUS_FAILED_ON_WITHDRAWING = -3; // 支付失敗
    public final static int STATUS_FAILED_ON_PROCESSING = -2; // 提交處理失敗
    public final static int STATUS_FAILED_ON_MERCHANT_CONFIRM = -1; // 商戶確認失敗
    public final static int STATUS_CREATED = 0; // 訂單生成
    public final static int STATUS_PROCESSING = 3; // 提交處理中
    public final static int STATUS_WITHDRAWING = 4; // 代付支付中
    public final static int STATUS_WITHDRAWN = 5; // 代付支付完成
    public final static int STATUS_COMPLETED = 6; // 業務處理完成


    public Integer getExposedStatus() {
        switch (status) {
            case STATUS_CREATED:


            case STATUS_PROCESSING:
            case STATUS_WITHDRAWING:
                return STATUS_WITHDRAWING;

            case STATUS_WITHDRAWN:
            case STATUS_COMPLETED:
                return STATUS_WITHDRAWN;

            case STATUS_FAILED_ON_WITHDRAWING:
            	return STATUS_FAILED_ON_WITHDRAWING;

            case STATUS_FAILED_ON_MERCHANT_CONFIRM:
            case STATUS_FAILED_ON_PROCESSING:
            case STATUS_FAILED_ON_SIGN:
            default:
                return STATUS_FAILED_ON_MERCHANT_CONFIRM;
        }
    }

    /**
     * 代付訂單狀態對應中文信息(for 後台)
     *
     * @return
     */
    public static String getStatusDesc(int status) {
    	switch (status) {
        case STATUS_CREATED:
        	return "代付支付中";
        case STATUS_PROCESSING:
        	return "提交處理中";
        case STATUS_WITHDRAWING:
        	return "代付支付中";
        case STATUS_WITHDRAWN:
        	return "支付完成";
        case STATUS_COMPLETED:
        	return "處理完成";
        case STATUS_FAILED_ON_MERCHANT_CONFIRM:
        	return "商戶確認失敗";
        case STATUS_FAILED_ON_PROCESSING:
        	return "創建失敗";
        case STATUS_FAILED_ON_WITHDRAWING:
        	return "支付失敗";
        case STATUS_FAILED_ON_SIGN:
        	return "驗證失敗";
        default:
            return String.valueOf(status);
    	}
    }

    /**
     * 狀態中文訊息
     * @return
     */
    public String getStatusDesc() {
    	return getStatusDesc(this.status);
    }

    private String withdrawOrderId; // 出款訂單號

    private Long accountId; // 帳戶ID
    private Integer status; // 狀態

    private String remark; // 備註訊息

    private String payeeCardNo; // 銀行卡卡號

    private String bankName; // 銀行名稱

    private String branchName; // 銀行支行名稱

    private String payeeCardName; // 銀行卡姓名

    private String bankProvince; // 銀行所在省

    private String bankCity; // 銀行所在市

    private BigDecimal amount; // 支付金額，單位分

    private BigDecimal actualAmount; // 實際支付金額，單位分

    private BigDecimal discountAmount; // 優惠金額，單位分

    private Double rate; // 手續費費率

    private BigDecimal rateFixedAmount; // 手續費，固定金額，單位分

    private BigDecimal fee; // 手續費，單位分

    private String rateDifferences; // 整個代理結構的費率差列表，JSON格式

    private String currency; // 三位貨幣代碼, 人民幣: cny

    private String notifyUrl; // 通知地址

    private Date successTime; // 訂單支付成功時間

    private String clientIp; // 客戶端IP

    private String clientDevice; // 客戶端設備

    private String clientExtra; // 客戶端發起時額外參數

    private Integer checkWithdrawnProcess; // 指出，是走哪個流程將訂單狀態設為WITHDRAWN

    private Date createTime; // 創建時間

    private Date updateTime; // 更新時間

    private String merchantUserId; //商戶的用戶ID

    private Integer withdrawMinute; // 提款分鐘連續請求
}
