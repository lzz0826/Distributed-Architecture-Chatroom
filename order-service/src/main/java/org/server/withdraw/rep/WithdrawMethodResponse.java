package org.server.withdraw.rep;

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
    private String returnCode; // 銀行方返回碼

    private String returnMessage; // 銀行方返回信息

    private String withdrawOrderId; // 出款訂單號, null if order not found

    private BigDecimal amount; // 訂單金額

    private Integer orderStatus; // 訂單狀態, null if order not found

    private String orderMsg; // 訂單狀態訊息, null if order not found
}
