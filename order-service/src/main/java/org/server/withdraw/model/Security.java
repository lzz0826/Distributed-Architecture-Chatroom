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
public class Security {
    /**
     * 商戶接口（含支付和代付）
     * 類型：白名單
     * 預設擋住所有的IP，只有在白名單裡面的IP才可以通過
     * Key: IP
     * Value: empty
     */
    public static final int TYPE_CREATE_ORDER = 0;

    /**
     * 系統賬戶登入
     * 類型：黑名單
     * 預設允許所有IP通過，只有在黑名單裡面的IP才會被擋住
     * Key: Username
     * Value: IP list
     */
    public static final int TYPE_USER_LOGIN = 1;

    /**
     * 代付卡號
     * 類型：黑名單
     * 預設在申請代付訂單時，允許所有卡號，只有在黑名單裡面的卡號才會被擋住
     * Key: Card No
     * Value: Cardholder name
     */
    public static final int TYPE_WITHDRAW_CARD_NO = 2;

    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ENABLED = 1;

    private String securityId; // 安全ID

    private Integer type; // 類型

    private Integer status; // 狀態

    private String securityKey; // Key

    private String securityValue; // Value

    private String memo; // 備註

    private Date createTime; // 創建時間

    private Date updateTime; // 更新時間
}
