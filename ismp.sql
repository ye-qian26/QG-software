DROP DATABASE IF EXISTS `ismp`;

CREATE SCHEMA `ismp` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE `ismp`;

-- 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,  -- 修改为自增
    `name` varchar(32) DEFAULT NULL,
    `password` varchar(20) DEFAULT NULL,
    `avatar` varchar(200) DEFAULT '/images/avatar/init.jpg',
    `email` varchar(200) DEFAULT NULL,
    `phone` varchar(11) DEFAULT NULL,
    `role` int DEFAULT '3',
    `money` double DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 关注表
DROP TABLE IF EXISTS `subscribe`;
CREATE TABLE `subscribe` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint DEFAULT NULL,
    `developer_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `subscribe_user_id_fk` (`user_id`),
    KEY `subscribe_user_id_fk_2` (`developer_id`),
    CONSTRAINT `subscribe_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `subscribe_user_id_fk_2` FOREIGN KEY (`developer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 软件表
DROP TABLE IF EXISTS `software`;
CREATE TABLE `software` (
    `id` bigint NOT NULL AUTO_INCREMENT,  -- 修改为自增
    `published_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `author_id` bigint DEFAULT NULL,
    `info` varchar(300) DEFAULT NULL,
    `price` double DEFAULT NULL,
    `link` varchar(200) DEFAULT NULL,
    `introduction` varchar(300) DEFAULT NULL,
    `version` varchar(20) DEFAULT NULL,
    `install_detail` varchar(300) DEFAULT NULL,
    `status` int DEFAULT '0',
    `picture` varchar(200) DEFAULT '/images/avatar/init.jpg',
    `type` varchar(100) DEFAULT '其他',
    `is_deleted` int DEFAULT '0',
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `software_user_id_fk` (`author_id`),
    CONSTRAINT `software_user_id_fk` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 评论表
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint DEFAULT NULL,
    `content` text,
    `software_id` bigint DEFAULT NULL,
    `time` datetime DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` int DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `review_software_id_fk` (`software_id`),
    KEY `review_user_id_fk` (`user_id`),
    CONSTRAINT `review_software_id_fk` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`),
    CONSTRAINT `review_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 订单表
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `software_id` bigint DEFAULT NULL,
    `price` double DEFAULT NULL,
    `time` datetime DEFAULT CURRENT_TIMESTAMP,
    `user_id` bigint DEFAULT NULL,
    `developer_id` bigint DEFAULT NULL,
    `is_deleted` int DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `order_software_id_fk` (`software_id`),
    KEY `order_user_id_fk` (`user_id`),
    KEY `order_user_id_fk_2` (`developer_id`),
    CONSTRAINT `order_software_id_fk` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`),
    CONSTRAINT `order_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `order_user_id_fk_2` FOREIGN KEY (`developer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 消息表
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` int NOT NULL AUTO_INCREMENT,
    `receiver_id` bigint DEFAULT NULL,
    `poster_id` bigint DEFAULT NULL,
    `content` text,
    `time` datetime DEFAULT CURRENT_TIMESTAMP,
    `is_read` int DEFAULT NULL,
    `is_deleted` int DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `message_user_id_fk` (`receiver_id`),
    KEY `message_user_id_fk_2` (`poster_id`),
    CONSTRAINT `message_user_id_fk` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
    CONSTRAINT `message_user_id_fk_2` FOREIGN KEY (`poster_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 设备表
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `software_id` bigint NOT NULL,
    `status` int DEFAULT NULL,
    `code1` varchar(100) DEFAULT NULL,
    `code2` varchar(100) DEFAULT NULL,
    `code3` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `equipment_software_id_fk` (`software_id`),
    KEY `equipment_user_id_fk` (`user_id`),
    CONSTRAINT `equipment_software_id_fk` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`),
    CONSTRAINT `equipment_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 封禁表
DROP TABLE IF EXISTS `ban`;
CREATE TABLE `ban` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint DEFAULT NULL,
    `start_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `end_time` datetime NOT NULL,
    `reason` text,
    `is_deleted` int DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `ban_user_id_fk` (`user_id`),
    CONSTRAINT `ban_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 软件申请表
DROP TABLE IF EXISTS `apply_software`;
CREATE TABLE `apply_software` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint DEFAULT NULL,
    `software_id` bigint NOT NULL,
    `reason` text,
    `material` varchar(100) NOT NULL,
    `status` int NOT NULL,
	`apply_time` datetime default CURRENT_TIMESTAMP null,
    PRIMARY KEY (`id`),
    KEY `apply_software_user_id_fk` (`user_id`),
    KEY `apply_software_software_id_fk` (`software_id`),
    CONSTRAINT `apply_software_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `apply_software_software_id_fk` FOREIGN KEY (`software_id`) REFERENCES `software` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 开发者申请表
DROP TABLE IF EXISTS `apply_developer`;
CREATE TABLE `apply_developer` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `apply_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `reason` text,
    `material` varchar(100) DEFAULT NULL,
    `status` int DEFAULT NULL,    
	`apply_time` datetime default CURRENT_TIMESTAMP null,
    PRIMARY KEY (`id`),
    KEY `apply_developer_user_id_fk` (`user_id`),
    CONSTRAINT `apply_developer_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 初始化数据
INSERT INTO `user` (`id`, `name`, `password`, `email`, `phone`, `role`, `money`) 
VALUES (1, 'admin', 'qgstudio', 'admin@qq.com', '18300000985', 1, 0),
(2, '张三', 'sss333', '13300001234@qq.com', '13300001234', 3, 333.33),
(3, '李开发', 'lll444', '14400001234@qq.com', '14400001234', 2, 444.44),
(4, '王开发', 'www555', '15500001234@qq.com', '14400001234', 2, 444.44),
(5, '赵六', 'zzz666', '16600001234@qq.com', '16600001234', 3, 444.44),
(6, '被封号', '123456', '19990001234@qq.com', '19990001234', 3, 10);

INSERT INTO `subscribe` (`user_id`, `developer_id`) VALUES (2, 3);
INSERT INTO `subscribe` (`user_id`, `developer_id`) VALUES (2, 4);

INSERT INTO `software` (`author_id`, `info`, `price`, `link`, `introduction`, `version`, `install_detail`, `name`, `status`) VALUES 
(3, '这是产品信息1.17', 114, 'localhost:8080', '这是一段介绍1.17', '1.17', '这是安装教程1.17', '老子的世界', '1'),
(3, '这是产品信息1.18', 514, 'localhost:8081', '这是一段介绍1.18', '1.18', '这是安装教程1.18', '老子的世界', '1'),
(3, '这是产品信息1.19', 1919, 'localhost:8082', '这是一段介绍1.19', '1.19', '这是安装教程1.19', '老子的世界', '1'),
(4, '这是产品信息4444', 810, 'localhost:8083', '这是一段介绍4444', '4.44', '这是安装教程4444', '图片生成表情包', '1'),
(4, '这是产品信息5555', 555, 'localhost:8083', '这是一段介绍5555', '5.55', '这是安装教程5555', '等待审核的安装包', '0');

INSERT INTO `ismp`.`apply_developer` (`user_id`, `reason`, `material`, `status`) VALUES ('2', '张三的理由', '/material/zhangsan.jpg', '0');
INSERT INTO `ismp`.`apply_software` (`user_id`, `software_id`, `reason`, `material`, `status`) VALUES ('4', '5', '申请发布软件理由', '/material/444.jpg', '0');

INSERT INTO `ismp`.`order` (`software_id`, `price`, `user_id`, `developer_id`) VALUES ('1', '114', '5', '3');
INSERT INTO `ismp`.`equipment` (`user_id`, `software_id`, `status`, `code1`) VALUES ('5', '1', '1', '19C8BEA2-681E-493B-8497-C4C6E6C15EF7');
INSERT INTO `ismp`.`review` (`user_id`, `content`, `software_id`) VALUES ('5', '给你打六星好评，这是多出来的那一颗', '1');
INSERT INTO `ismp`.`ban` (`user_id`, `end_time`, `reason`) VALUES ('6', '9999-12-31 23:59:59', '原因');
INSERT INTO `ismp`.`equipment` (`user_id`, `software_id`, `status`) VALUES ('5', '4', '0');
INSERT INTO `ismp`.`message` (`receiver_id`, `poster_id`, `content`, `is_read`) VALUES ('5', '3', '您预约的id为1的软件已发布，快去购买吧', '1');

