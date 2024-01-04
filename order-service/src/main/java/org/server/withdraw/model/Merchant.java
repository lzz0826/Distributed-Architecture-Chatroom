package org.server.withdraw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Merchant {
    public static final String PATH_SEPARATOR = "/";

    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ENABLED = 1;

    public static final int DISCOUNT_STATUS_DISABLED = 0;
    public static final int DISCOUNT_STATUS_ENABLED = 1;

    private String merchantId; // 商户ID

    private String merchantName; // 商戶名称

    private String userId; // 管理者帳號(使用者)

    private String requestKey; // 请求私钥

    private String publicKey; // rsa公钥

    private String privateKey; // rsa私钥

    private BigDecimal balance; //余额

    private BigDecimal frozenAmount; // 冻结金额

    private String bankName; // 銀行名稱

    private String payeeCardName; // 銀行帳戶名稱

    private String payeeCardNo; // 銀行卡號

    private Integer status; // 状态

    private String memo; // 備註

    private String mobile; // 手機號碼

    private String email; // 電子郵箱

    private Date createTime; // 創建時間

    private Date updateTime; // 更新時間

    private String creator; // 創建人

    private String updater; // 更新人

    private Integer isBackendLogin; //是否為後台可登

    private String password;

    private String sort;

}
