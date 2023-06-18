# Distributed-Architecture
# 分布式架構

# 1.使用 docker compose up -d 分別啟用 docker裡的 Mysql , Redis .Nacos

使用的表結構

CREATE DATABASE `cloud_order`

CREATE TABLE `tb_order` (
  `id` varchar(40) NOT NULL COMMENT 'orderId',
  `user_id` varchar(40) DEFAULT NULL COMMENT 'userId',
  `name` varchar(40) DEFAULT NULL COMMENT '訂單名',
  `price` int DEFAULT NULL COMMENT '價錢',
  `num` int DEFAULT NULL COMMENT '數',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用者';

CREATE DATABASE `cloud_user` 

CREATE TABLE `tb_user` (
  `id` varchar(40) NOT NULL COMMENT 'userId',
  `password` varchar(40) DEFAULT NULL,
  `username` varchar(40) DEFAULT NULL COMMENT '用戶名增',
  `address` varchar(40) DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='使用者';



