-- 选择要使用的数据库
USE cloud_order;

-- 訂單表
CREATE TABLE IF NOT EXISTS `tb_order` (
    `id` VARCHAR(40) NOT NULL COMMENT 'orderId',
    `user_id` VARCHAR(40) DEFAULT NULL COMMENT 'userID',
    `wallet_id` VARCHAR(40) DEFAULT NULL COMMENT 'walletID',
    `target_user_id` VARCHAR(40) DEFAULT NULL COMMENT '接收用户ID',
    `target_wallet_id` VARCHAR(40) DEFAULT NULL COMMENT '接收钱包ID',
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

