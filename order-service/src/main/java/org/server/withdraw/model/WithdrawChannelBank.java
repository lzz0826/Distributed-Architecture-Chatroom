package org.server.withdraw.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawChannelBank {
    //渠道方 的銀行資訊

    private String withdrawChannelBankId; // 代付銀行表ID

    private String withdrawBankChannelId; // 對應到t_withdraw_bank_channel.id

    private String bankCodeId; // 對應到t_withdraw_channel_bank_code.id

    private String bankName; //銀行名

    private String bankCode; // 銀行聯行碼

    private Integer status; //狀態 0:啟用 1:禁用

    private String orderOriginationUrl; // 訂單發起URL

    private String orderQueryUrl; // 訂單查詢URL

    private String balanceQueryUrl; // 餘額查詢URL

    private String updater; // 更新人

    private String creator; // 創建人

    private String createIp; // 新增IP

    private String updateIp; // 更新者IP

    private String ourNotifyUrl; //我方回調 url

    private String memo; //備註

    private Date updateTime; // 更新時間

    private Date createTime; // 創建時間



}