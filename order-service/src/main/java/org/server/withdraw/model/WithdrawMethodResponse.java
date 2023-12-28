package org.server.withdraw.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WithdrawMethodResponse {
    private String returnCode; // 第三方返回碼

    private String returnMessage; // 第三方返回信息

    private String withdrawOrderId; // 代付訂單號, null if order not found

    private String channelOrderNo; // 代付渠道訂單號

    private BigDecimal amount; // 訂單金額，單位分

    private Integer orderStatus; // 訂單狀態, null if order not found

    private String orderMsg; // 訂單狀態訊息, null if order not found
}
