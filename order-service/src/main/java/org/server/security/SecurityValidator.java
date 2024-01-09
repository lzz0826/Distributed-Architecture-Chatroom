package org.server.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.server.mapper.SecurityMapper;
import org.server.utils.SecurityIpHelper;
import org.server.withdraw.model.Security;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SecurityValidator {
    @Resource
    private SecurityMapper securityMapper;

    private Map<String, SecurityIpHelper> createOrderMap;

    private Set<String> cardNoSet;

    @PostConstruct
    public void loadSecurityData() {
        log.info("Initializing security IP list");

        Map<String, SecurityIpHelper> createOrderMap = new HashMap<>();
        Set<String> cardNoSet = new HashSet<>();

        Security query = Security.builder()
                .status(Security.STATUS_ENABLED)
                .build();
        List<Security> list = securityMapper.findSecurity(query);
        for (Security security : list) {
            switch (security.getType()) {
                case Security.TYPE_CREATE_ORDER:
                    String ips = security.getSecurityValue();
                    if (ips != null && ips.length() > 0) {
                        createOrderMap.put(security.getSecurityKey(), new SecurityIpHelper(ips));
                    }
                    break;

                case Security.TYPE_WITHDRAW_CARD_NO:
                    cardNoSet.add(security.getSecurityKey());
                    break;

                default:
            }
        }

        this.createOrderMap = createOrderMap;
        this.cardNoSet = cardNoSet;
    }

    public boolean verifyIp(String merchantId, String ip) {
        SecurityIpHelper helper = createOrderMap.get(merchantId);
        return helper != null && helper.match(ip);
    }

    public boolean verifyCardNo(String cardNo) {
        return !cardNoSet.contains(cardNo.trim());
    }
}
