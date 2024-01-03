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
    private String accountId; //TODO 銀行帳號Id  暫時重這取

    @NotBlank
    private String clientIp;

    @NotBlank
    private String clientDevice;

    @NotBlank
    private String orderNo; //单号

    @NotNull
    @Min(100)
    private BigDecimal amount; // 提現金额（单位分）

    @NotBlank
    private String currency; // 币別

    private String clientExtra; // 特定渠道发起时额外参数

    @NotBlank
    private String payeeCardNo; // 銀行卡卡号

    @NotBlank
    private String payeeCardName; // 銀行卡持卡人姓名

    private String bankName; // 银行名称

    private String branchName; // 支行名称

    private String bankProvince; // 銀行所在省

    private String bankCity; // 銀行所在市

    @URL
    @NotBlank
    private String notifyUrl; // 提現结果回调URL


    @NotBlank
    private String sign; // 签名
}
