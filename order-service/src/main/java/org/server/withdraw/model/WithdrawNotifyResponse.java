package org.server.withdraw.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawNotifyResponse {
    private String withdrawOrderId;

    private String channelOrderNo;

    private BigDecimal amount;

    private Integer status;
}
