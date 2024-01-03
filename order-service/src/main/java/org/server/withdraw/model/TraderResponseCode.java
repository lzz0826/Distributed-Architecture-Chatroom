package org.server.withdraw.model;

public enum TraderResponseCode {
    // 成功
    SUCCESS("0000", ""),

    SIGN_VERIFICATION_FAILED("0001", "签名验证失败"),
    DB_FAIL("0002", "数据库操作失败"),

    PARAM_NOT_FOUND("0101", "参数不存在"),
    PARAM_INVALID("0102", "无效的参数"),
    PARAM_EMPTY("0103", "参数不可留白"),

    MERCHANT_NOT_FOUND("0201", "商户不存在"),
    MERCHANT_SETTLEMENT_NOT_FOUND("0202", "（商户，渠道）设定不存在"),
    MERCHANT_INVALID_AMOUNT("0203", "已超出商户单笔限额"),
    MERCHANT_INVALID_DAY_AMOUNT("0204", "已超出商户单日限额"),
    MERCHANT_INVALID_DAY_COUNT("0205", "已超出商户单日限笔数"),
    MERCHANT_DISABLED("0206", "商户未启用"),
    MERCHANT_WITHDRAW_NOT_SUPPORTED("0207", "商户未启用代付"),
    MERCHANT_BALANCE_NOT_ENOUGH("0209", "商户余额不足"),
    MERCHANT_FROZEN_AMOUNT_NOT_ENOUGH("0210", "商户冻结金额不足"),
    MERCHANT_IP_BLOCK("0211", "非白名单IP"),
    MERCHANT_USER_DISABLED("0212", "该商户的用户已被禁用"),
    MERCHANT_CONFIRMATION_FAILED("0213", "商户的代付确认失败"),
    MERCHANT_CONFIRMA_WITHDRAW_ORDER_FAILED("0214", "商户端未确认此笔代付订单"),

    CHANNEL_NOT_FOUND("0301", " 渠道不支援"),
    CHANNEL_TYPE_NOT_SUPPORTED("0302", "渠道型态不支援"),
    CHANNEL_INVALID_PARAM("0303", "渠道参数验证不通过"),
    CHANNEL_TYPE_INVALID_PARAM("0304", "特定渠道参数验证不通过"),

    ACCOUNT_NOT_FOUND("0401", "渠道不存在"),
    ACCOUNT_NOT_FOUND_USABLE("0402", "找不到可用渠道"),
    ACCOUNT_NOT_ENABLED("0403", "渠道非启用状态"),
    ACCOUNT_GROUP_CODE_NOT_FOUND("0404", "群组代码不存在"),
    ACCOUNT_NOT_FOUND_QUOTA("0405", "找不到符合此金额或额度的可用渠道"),
    ACCOUNT_NOT_FOUND_CHANNEL_TYPE("0406", " 找不到支援此签约的可用渠道"),
    ACCOUNT_NOT_FOUND_SPECIFIED_AMOUNT("0407", "找不到支援此指定金额的可用渠道"),

    ORDER_NOT_FOUND("0501", "订单不存在"),
    ORDER_INCORRECT_STATUS("0502", "订单状态不正确"),
    MERCHANT_ORDER_NO_DUPLICATE("0503", "商户订单号重复试"), // 商户订单号重复
    CREATE_ORDER_WITH_BANNED_IP("0504", "IP禁止发起订单"), // IP禁止发起订单
    CREATE_ORDER_WITH_DIFFERENT_IP("0505", "IP比对不一致"), // IP比对不一致
    CREATE_ORDER_WITH_BANNED_USER("0506", "用户被BAN"),

    QUERY_ALIPAY_ORDER_FAILED("0601", "查询支付宝订单失败"),

    MERCHANT_NOTIFY_NOT_FOUND("0701", "通知单不存在"),

    WITHDRAW_CHANNEL_NOT_FOUND_USABLE("0801", "找不到可用出款銀行渠道"),
    WITHDRAW_CHANNEL_NOT_STABLE("0802", "代付线路不稳定"),
    WITHDRAW_CHANNEL_INVALID_CARD_NO("0804", "不合法卡号"),
    WITHDRAW_CHANNEL_BALANCE_NOT_ENOUGH("0805", "出款銀行渠道余额不足"),
    WITHDRAW_CHANNEL_NOT_FOUND("0806", "代付渠道不存在"),
    WITHDRAW_CHANNEL_SIGN_VERIFICATION_FAILED("0807", "代付渠道方回应签名签证失败"),
    WITHDRAW_CHANNEL_SEARCH_ORDER_FAILED("0808", "查询渠道方订单失败"),
    WITHDRAW_CHANNEL_AMOUNT_INVALID("0809", "代付渠道不支援此金额"),
    WITHDRAW_CHANNEL_UNAVAILABLE_AMOUNT("0810", "代付渠道不支持此金额"),
    WITHDRAW_CHANNEL_UNAVAILABLE_TIME("0811", "代付渠道为非开放时间"),
    WITHDRAW_CHANNEL_CANNOT_GET_BALANCE("0812", "代付线路不稳定无法取得余额"),
    WITHDRAW_REQUEST_ORDER_FAIL("0813","发起代付渠道方订单失败"),
    WITHDRAW_REQUEST_GEN_SIGN_FAIL("0814","进行第三方代付订单加密时发生错误"),
    WITHDRAW_REQUEST_RETURN_FAIL("0815","进行第三方代付订单退回错误"),
    WITHDRAW_REQUEST_CANCEL_FAIL("0816","进行第三方代付订单取消错误"),
    WITHDRAW_CHANNEL_NOT_OPEN("0817","代付渠道未启用"),
    WITHDRAW_CHANNEL_NOT_MATCHING_BANK_NAME("0818", "代付渠道无匹配银行名称"),
    WITHDRAW_CHANNEL_BANK_NAME_IS_REQUIRED("0819", "代付渠道银行名称为必填"),
    WITHDRAW_CHANNEL_BRANCH_NAME_IS_REQUIRED("0820", "代付渠道銀行支行名稱为必填"),
    WITHDRAW_CHANNEL_BANK_PROVINCE_IS_REQUIRED("0821", "代付渠道銀行所在省为必填"),
    WITHDRAW_CHANNEL_BANK_CITY_IS_REQUIRED("0822", "代付渠道銀行所在市为必填"),
    WITHDRAW_CHANNEL_SIGN_ERROR("0823", "加签失败"),

    BANK_NOT_SUPPORTED("0901", "无可支持该银行的代付渠道"),
    CARD_NUMBER_LIMIT("0902", "该卡号已达今日上限"),

    PAY_ORDER_HAS_PAID("1001", "订单状态为已支付或已完成"),
    PAY_FAILED("1002", "发起渠道方订单失败"),
    PAY_GET_ORDER_FAILED("1003", "查询渠道方订单失败"),
    PAY_GET_ORDER_TIMEOUT_FAILED("1003", "查询渠道方订单超时"),
    PAY_SIGN_VERIFICATION_FAILED("1004", "渠道方回应签名签证失败"),
    PAY_GEN_SIGN_VERIFICATION_FAILED("1005", "使用商户私钥加密失败"),
    PAY_VENDOR_CHANNEL_CODE_NOT_FOUND("1006","无法取得第三方渠道商所须的ChannelCode"),
    PAY_SUCCESS_AMOUNT_ABNORMAL_FROM_VENDOR("1007", "查询渠道方订单的支付金额发生异常"),
    PAY_AMOUNT_NOT_SUPPORT_CENT("1008", "渠道订单金额不得为分"),

    WY_CAPTCHA_PARSING_FAILED("1101", "验证码解析错误"),
    WY_PAY_FAILED("1102", "支付失败"),
    WY_ACCOUNT_GETTING_TOKEN_FAILED("1103", "账号取得Token失败"),
    WY_ACCOUNT_IN_USE("1104", "账号忙碌中"),
    WY_CAPTCHA_INCORRECT("1105", "验证码错误"),
    WY_ACCOUNT_NOT_LOGGED_IN("1106", "账号未登陆"),
    WY_ACCOUNT_LOGGING_IN("1107", "账号登陆中"),
    WY_PAY_NOT_SUPPORT("1108", "选择的支付方式暂时无法使用"),
    WY_NOT_FOUND_CHANNEL_ORDER("1109", "查询不到渠道订单"),

    BANKCARD_NOT_ALLOWED_TO_CREATE_ORDER("1201", "此银行卡不可用于发单"),

    WITHDRAW_METHOD_INVALID_SIGN("1301", "第三方代付回应验证失败"),
    WITHDRAW_METHOD_GET_BALANCE_FAILED("1302", "查询第三方代付余额失败"),
    WITHDRAW_METHOD_RESPONSE_DATA_ZONE("1303", "第三方代付response資料欄為空"),
    WITHDRAW_METHOD_REQUEST_DATA_ZONE("1304", "第三方代付request資料欄為空"),
    XML_TO_OBJECT_FAILED("1305", "Xml轉換物件失敗"),

    // QA Test
   	QA_TEST_PAY_ORDER_STATUS_0("QA001","模拟订单状态为'创建中'完成!"),
   	QA_TEST_PAY_ORDER_STATUS_13("QA002","模拟订单状态为'创建失败'完成!"),
   	QA_TEST_PAY_ORDER_NOTIFY_FAIL("QA003","模拟回調通知'商戶端失敗'完成"),

    WITHDRAW_METHOD_RESPONSE_FAIL("1400", "渠道错误:"),
    WITHDRAW_METHOD_RESPONSE_ORDER_NOT_EXIST_FAIL("1401", "渠道超时错误:验证后为订单不存在"),

    WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_CONTINUOUS("1402", "代付渠道銀行卡连续请求限制"),

    WITHDRAW_METHOD_PAYEE_CARD_NO_BANK_QUANTITY_CONTINUOUS("1403", "代付渠道銀行卡请求数量限制"),

    WITHDRAW_ORDER_VERIFICATION_FAILED("0001", "订单验证失败"),

    // 未知错误
    UNKNOWN_ERROR("9999", "未知错误");

    private String code;
    private String message;

    TraderResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
