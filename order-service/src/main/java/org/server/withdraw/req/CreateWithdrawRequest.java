package org.server.withdraw.req;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import org.server.withdraw.dto.DtoSigner;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateWithdrawRequest extends DtoSigner {
    @NotBlank
    private String userId; //使用者ID

    @NotBlank
    private String payeeCardNo; // 銀行卡卡号

    @NotBlank
    private String payeeCardName; // 銀行卡持卡人姓名

    private String branchName; // 支行名称

    private String bankProvince; // 銀行所在省

    private String bankCity; // 銀行所在市

    //----我方轉出款相關
    /**
     * 出款帳號ID(我方)
     */
    @NotBlank
    private String accountId;
    /**
     * 出款人名稱(我方)
     */
    @NotBlank
    private String accountName;
    /**
     * 出款銀行代碼(我方)
     */
    @NotBlank
    private String bankCode;

    /**
     * 出款銀行名稱(我方)
     */
    @NotBlank
    private String bankName;

    //----轉入方相關
    /**
     * 收款帳號ID(對方)
     */
    @NotBlank
    private String targetAccountId;
    /**
     * 收款人名稱(對方)
     */
    @NotBlank
    private String targetAccountName;
    /**
     * 收款銀行代碼(對方)
     */
    @NotBlank
    private String targetBankCode;
    /**
     * 收款銀行名稱(對方)
     */
    @NotBlank
    private String targetBankName;

    private String clientIp;

    private String clientDevice;

    private String orderNo; //单号

    @NotNull
    @Min(100)
    private BigDecimal amount; // 提現金额（单位分）

    @NotBlank
    private String currency; // 币別

    private String clientExtra; // 特定渠道发起时额外参数


    @URL
    @NotBlank
    private String notifyUrl; // 提現结果回调URL


    @NotBlank
    private String sign; // 签名
}
