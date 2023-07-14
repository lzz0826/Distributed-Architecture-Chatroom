-- 选择要使用的数据库
USE cloud_user;

-- 创建用户表
CREATE TABLE IF NOT EXISTS `tb_user` (
    `id` varchar(40) NOT NULL COMMENT 'userId',
    `password` varchar(40) DEFAULT NULL,
    `username` varchar(40) DEFAULT NULL COMMENT '用戶名增',
    `address` varchar(40) DEFAULT NULL COMMENT '地址',
    `role` varchar(40) DEFAULT NULL,
    `update_time` timestamp NOT NULL,
    `create_time` timestamp NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `tb_username` (`username`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用者';

-- 插入用户数据
INSERT INTO tb_user
(id, password, username, address, `role`, update_time, create_time)
VALUES('3099538860151615210', '25d55ad283aa400af464c76d713c07ad', 'tom', '', 'ROLE_ADMIN', '2023-06-20 06:40:24', '2023-06-20 06:40:24');

INSERT INTO tb_user
(id, password, username, address, `role`, update_time, create_time)
VALUES('3099540442310192874', '25d55ad283aa400af464c76d713c07ad', 'tony', '', 'ROLE_ADMIN', '2023-06-20 06:40:24', '2023-06-20 06:40:24');

-- 创建聊天室表
CREATE TABLE IF NOT EXISTS `tb_chatroom` (
    `id` varchar(40) NOT NULL COMMENT '聊天室ID',
    `name` varchar(40) DEFAULT NULL COMMENT '聊天室名',
    `admin_user_id` varchar(40) DEFAULT NULL COMMENT '管理者Id',
    `status` tinyint(4) DEFAULT NULL COMMENT '狀態',
    `update_time` timestamp NOT NULL COMMENT '更新時間',
    `create_time` timestamp NOT NULL COMMENT '創建時間',
    PRIMARY KEY (`id`),
    UNIQUE KEY `tb_name` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天室';

-- 创建黑名单表
CREATE TABLE IF NOT EXISTS `tb_black_list` (
    `id` varchar(40) NOT NULL COMMENT 'chatroomId',
    `user_id` varchar(40) DEFAULT NULL COMMENT 'suerID',
    `blacklist` varchar(40) DEFAULT NULL COMMENT '黑名單',
    `update_time` timestamp NOT NULL COMMENT '更新時間',
    `create_time` timestamp NOT NULL COMMENT '創建時間',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uc_user_blacklist` (`user_id`,`blacklist`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人際關係';

-- 创建聊天记录表
CREATE TABLE IF NOT EXISTS `tb_chat_record` (
    `id` varchar(40) NOT NULL COMMENT '聊天紀錄id',
    `user_id` varchar(40) DEFAULT NULL COMMENT '發送者的userId',
    `receiver_user_id` varchar(40) DEFAULT NULL COMMENT '接收者的userId',
    `chatroom_id` varchar(40) DEFAULT NULL COMMENT '聊天室Id',
    `msg_type` varchar(40) DEFAULT NULL COMMENT '訊息種類chatroom.privateChat.all',
    `content` text COMMENT '內容',
    `status` tinyint(4) DEFAULT NULL COMMENT '狀態',
    `update_time` timestamp NOT NULL COMMENT '更新時間',
    `create_time` timestamp NOT NULL COMMENT '創建時間',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天記錄';
