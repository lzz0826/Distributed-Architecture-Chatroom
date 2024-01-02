-- 選擇要使用的數據庫
USE cloud_order;

-- 訂單表
CREATE TABLE IF NOT EXISTS `tb_order` (
    `id` VARCHAR(40) NOT NULL COMMENT 'orderId',
    `user_id` VARCHAR(40) DEFAULT NULL COMMENT 'userID',
    `wallet_id` VARCHAR(40) DEFAULT NULL COMMENT 'walletID',
    `target_user_id` VARCHAR(40) DEFAULT NULL COMMENT '接收用戶ID',
    `target_wallet_id` VARCHAR(40) DEFAULT NULL COMMENT '接收錢包ID',
    `price` DECIMAL(10, 2) NOT NULL NOT NULL COMMENT '交易金額',
    `payment_method` VARCHAR(50) DEFAULT NULL COMMENT '支付方式',
    `type` TINYINT(1) DEFAULT NULL COMMENT '訂單類型',
    `status` TINYINT(1) DEFAULT NULL COMMENT '訂單狀態',
    `update_time` TIMESTAMP NOT NULL,
    `create_time` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='訂單表';

-- 錢包表
CREATE TABLE IF NOT EXISTS `tb_wallets` (
                         `wallet_id` VARCHAR(40) NOT NULL COMMENT 'wallet_id',
                         `user_id` VARCHAR(40) DEFAULT NULL COMMENT 'userID',
                         `balance` DECIMAL(10, 2) NOT NULL COMMENT '餘額',
                         `status` TINYINT(1) DEFAULT NULL COMMENT '錢包狀態',
                         `update_time` TIMESTAMP NOT NULL,
                         `create_time` TIMESTAMP NOT NULL,
                         PRIMARY KEY (`wallet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='錢包表';

-- 交易記錄表
CREATE TABLE IF NOT EXISTS `tb_transactions` (
                              `transaction_id` VARCHAR(40) NOT NULL COMMENT 'transaction_id',
                              `user_id` VARCHAR(40) DEFAULT NULL COMMENT 'userID',
                              `wallet_id` VARCHAR(40) DEFAULT NULL COMMENT 'wallet_id',
                              `amount` DECIMAL(10, 2) NOT NULL NOT NULL COMMENT '交易金額',
                              `payment_method` VARCHAR(50) DEFAULT NULL COMMENT '支付方式',
                              `type` TINYINT(1) DEFAULT NULL COMMENT '交易類型',
                              `status` TINYINT(1) DEFAULT NULL COMMENT '交易狀態',
                              `description` TEXT DEFAULT NULL COMMENT '交易描述',
                              `update_time` TIMESTAMP NOT NULL,
                              `create_time` TIMESTAMP NOT NULL,
                              PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易記錄表';



-- 銀行卡列表 bank_card_account_id(相當於銀行帳號) 一個人在一家銀行只會有一個帳戶
CREATE TABLE IF NOT EXISTS  `tb_bankcard_account` (
                                                      `bank_card_account_id` VARCHAR(40) NOT NULL COMMENT '存款卡ID(相當於銀行帳號)',
                                                      `bank_id` VARCHAR(40) DEFAULT NULL COMMENT '銀行ID',
                                                      `card_id` varchar(100) DEFAULT NULL COMMENT '存款卡編號(相當於銀行帳號)',
                                                      `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '類型: 0:收款卡, 1:中轉卡, 2:安全卡, 3:付款卡',
                                                      `payee_card_name` varchar(100) DEFAULT NULL COMMENT '用戶名',
                                                      `payee_card_no` varchar(100) DEFAULT NULL COMMENT '銀行卡號(相當於信用卡)',
                                                      `bank_name` varchar(100) DEFAULT NULL COMMENT '銀行名稱',
                                                      `bank_area` varchar(100) DEFAULT NULL COMMENT '地區名稱',
                                                      `branch_name` varchar(100) DEFAULT NULL COMMENT '分行名稱',
                                                      `status` int(11) NOT NULL DEFAULT '0' COMMENT '狀態 0:下架 1:啟用 2:達標 3:風控 4:暫時禁用 5:冷卻 6:金額冷卻',
                                                      `memo` varchar(255) DEFAULT NULL COMMENT '備註',
                                                      `balance` DECIMAL(10, 2) NOT NULL NOT NULL COMMENT '卡片餘額',
                                                      `login_username` varchar(100) DEFAULT NULL COMMENT '登入帳號',
                                                      `login_password` varchar(100) DEFAULT NULL COMMENT '登入密碼',
                                                      `transaction_password` varchar(30) DEFAULT NULL COMMENT '交易密碼',
                                                      `mobile` varchar(30) DEFAULT NULL COMMENT '手機號碼',
                                                      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
                                                      `update_time` timestamp NULL DEFAULT NULL COMMENT '更新時間',
                                                      `creator` varchar(30) DEFAULT NULL COMMENT '創建人',
                                                      `updater` varchar(30) DEFAULT NULL COMMENT '更新人',
                                                      PRIMARY KEY (`bank_card_account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='銀行卡列表';


-- 銀行代碼列表
CREATE TABLE IF NOT EXISTS `tb_bank_code` (
                                                  `bank_id` VARCHAR(40) NOT NULL COMMENT 'bankId',
                                                  `bank_code` varchar(20) DEFAULT NULL COMMENT '銀行聯行碼',
                                                  `bank_abbreviation` varchar(10) DEFAULT NULL COMMENT '銀行名稱縮寫',
                                                  `bank_name` varchar(20) DEFAULT NULL COMMENT '銀行名稱',
                                                  `request_key` varchar(20) DEFAULT NULL COMMENT '請求私鑰',
                                                  `public_key` varchar(20) DEFAULT NULL COMMENT 'RSA公鑰',
                                                  `private_key` varchar(20) DEFAULT NULL COMMENT 'RSA私鑰',
                                                  `status` varchar(20) DEFAULT NULL COMMENT '銀行使用狀態',
                                                  `update_time` TIMESTAMP NOT NULL,
                                                  `create_time` TIMESTAMP NOT NULL,
                                                  `creator` varchar(30) DEFAULT NULL COMMENT '創建人',
                                                  `updater` varchar(30) DEFAULT NULL COMMENT '更新人',
                                                  PRIMARY KEY (`bank_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='銀行代碼列表';


-- 取款訂單列表
CREATE TABLE IF NOT EXISTS `t_withdraw_order` (
                                                  `withdraw_order_id` varchar(30) NOT NULL COMMENT '取款訂單號',
                                                  `user_id` varchar(30) DEFAULT NULL COMMENT '商戶ID',
                                                  `bank_order_no` varchar(30) DEFAULT NULL COMMENT '銀行方的訂單號',
                                                  `bank_return_code` varchar(10) DEFAULT NULL COMMENT '銀行方回傳Cod',
                                                  `bank_return_message` text COMMENT '銀行方回傳信息',
                                                  `remark` blob COMMENT '備註信息',
                                                  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '代付狀態，-4:驗證失敗，-3:代付支付失敗，-2:代付申請失敗，-1:商戶確認失敗，0:訂單生成，1:商戶確認成功，2:代付申請成功，3:代付支付完成，4:業務處理完成',
                                                  `payee_card_no` varchar(20) DEFAULT NULL COMMENT '銀行卡卡號',
                                                  `bank_name` varchar(20) DEFAULT NULL COMMENT '銀行名稱',
                                                  `branch_name` varchar(20) DEFAULT NULL COMMENT '銀行支行名稱',
                                                  `payee_card_name` varchar(40) DEFAULT NULL COMMENT '銀行卡姓名',
                                                  `bank_province` varchar(20) DEFAULT NULL COMMENT '銀行所在省',
                                                  `bank_city` varchar(20) DEFAULT NULL COMMENT '銀行所在市',
                                                  `bank_code` varchar(20) DEFAULT NULL COMMENT '我方銀行聯行碼',
                                                  `amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '取款金額，單位分',
                                                  `actual_amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '實際取款金額，單位分',
                                                  `rate` double NOT NULL DEFAULT '0' COMMENT '手續費費率',
                                                  `rate_fixed_amount` bigint(20) NOT NULL DEFAULT '0' COMMENT '手續費固定金額，單位分',
                                                  `notify_url` varchar(128) DEFAULT NULL COMMENT '通知地址',
                                                  `success_time` timestamp NULL DEFAULT NULL COMMENT '訂單代付成功時間',
                                                  `client_ip` varchar(32) DEFAULT NULL COMMENT '客戶端IP',
                                                  `client_device` varchar(64) DEFAULT NULL COMMENT '客戶端設備',
                                                  `client_extra` blob COMMENT '渠道方要求的額外參數（JSON格式）',
                                                  `create_time` timestamp NULL DEFAULT NULL COMMENT '創建時間',
                                                  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新時間',
                                                  PRIMARY KEY (`withdraw_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代付訂單';

