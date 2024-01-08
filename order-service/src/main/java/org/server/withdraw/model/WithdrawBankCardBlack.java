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
public class WithdrawBankCardBlack {

    private String id; // 出款銀行卡黑名單id

    private String merchantId; // 商戶ID

    private String bankCardNo; // 銀行卡卡號

    private String memo;

    private String createIp;

    private String updateIp;

    private String creator;

    private String updater;

    private Date createTime;

    private Date updateTime;
}
