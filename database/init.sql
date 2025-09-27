-- 旅牛网数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `ln_travel` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `ln_travel`;

-- 注意：请在云服务器上执行此脚本
-- 服务器地址：121.43.245.176
-- 数据库密码：lninfo123

-- 用户表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `gender` tinyint DEFAULT '0' COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户足迹表
CREATE TABLE `user_footprint` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '足迹ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景区ID',
  `visit_date` date NOT NULL COMMENT '访问日期',
  `visit_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户足迹表';

-- 用户收藏表
CREATE TABLE `user_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景区ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_scenic` (`user_id`, `scenic_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户收藏表';

-- 景区分类表
CREATE TABLE `scenic_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `level` varchar(10) NOT NULL COMMENT '景区等级：3A,4A,5A',
  `description` text COMMENT '分类描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_level` (`name`, `level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景区分类表';

-- 景区表
CREATE TABLE `scenic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '景区ID',
  `name` varchar(100) NOT NULL COMMENT '景区名称',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `province` varchar(50) NOT NULL COMMENT '省份',
  `city` varchar(50) NOT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `open_time` varchar(100) DEFAULT NULL COMMENT '开放时间',
  `description` text COMMENT '景区描述',
  `facilities` text COMMENT '服务设施（JSON格式）',
  `images` text COMMENT '景区图片（JSON格式）',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '评分',
  `visit_count` int DEFAULT '0' COMMENT '访问次数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-关闭，1-开放',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_province_city` (`province`, `city`),
  KEY `idx_rating` (`rating`),
  KEY `idx_visit_count` (`visit_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景区表';

-- 门票表
CREATE TABLE `ticket` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '门票ID',
  `scenic_id` bigint NOT NULL COMMENT '景区ID',
  `name` varchar(100) NOT NULL COMMENT '门票名称',
  `type` varchar(20) NOT NULL COMMENT '门票类型：adult-成人票，child-儿童票，student-学生票，senior-老人票',
  `price` decimal(10,2) NOT NULL COMMENT '门票价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `description` text COMMENT '门票描述',
  `valid_days` int DEFAULT '1' COMMENT '有效天数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门票表';

-- 秒杀活动表
CREATE TABLE `seckill_activity` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `ticket_id` bigint NOT NULL COMMENT '门票ID',
  `title` varchar(100) NOT NULL COMMENT '活动标题',
  `seckill_price` decimal(10,2) NOT NULL COMMENT '秒杀价格',
  `stock` int NOT NULL COMMENT '库存数量',
  `sold` int DEFAULT '0' COMMENT '已售数量',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-未开始，1-进行中，2-已结束',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_start_end_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='秒杀活动表';

-- 订单表
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景区ID',
  `ticket_id` bigint NOT NULL COMMENT '门票ID',
  `quantity` int NOT NULL COMMENT '购买数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `visit_date` date NOT NULL COMMENT '游览日期',
  `visitor_info` text COMMENT '游客信息（JSON格式）',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待支付，PAID-已支付，USED-已使用，CANCELLED-已取消，REFUNDED-已退款',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 评论表
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景区ID',
  `order_id` bigint DEFAULT NULL COMMENT '订单ID',
  `content` text NOT NULL COMMENT '评论内容',
  `rating` tinyint NOT NULL COMMENT '评分：1-5星',
  `images` text COMMENT '评论图片（JSON格式）',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `reply_count` int DEFAULT '0' COMMENT '回复数',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-隐藏，1-显示',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 评论回复表
CREATE TABLE `comment_reply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `comment_id` bigint NOT NULL COMMENT '评论ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '回复内容',
  `parent_id` bigint DEFAULT NULL COMMENT '父回复ID',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_comment_id` (`comment_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论回复表';

-- 推荐记录表
CREATE TABLE `recommend_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '推荐ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `scenic_id` bigint NOT NULL COMMENT '景区ID',
  `recommend_type` varchar(20) NOT NULL COMMENT '推荐类型：personal-个性化，hot-热门，ai-AI推荐',
  `recommend_reason` varchar(255) DEFAULT NULL COMMENT '推荐理由',
  `score` decimal(5,2) DEFAULT NULL COMMENT '推荐分数',
  `is_clicked` tinyint DEFAULT '0' COMMENT '是否点击：0-未点击，1-已点击',
  `click_time` datetime DEFAULT NULL COMMENT '点击时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_recommend_type` (`recommend_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='推荐记录表';

-- AI对话记录表
CREATE TABLE `ai_chat_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '对话ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `session_id` varchar(32) NOT NULL COMMENT '会话ID',
  `message` text NOT NULL COMMENT '用户消息',
  `reply` text NOT NULL COMMENT 'AI回复',
  `recommendations` text COMMENT '推荐结果（JSON格式）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI对话记录表';

-- 数据统计表
CREATE TABLE `data_statistics` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `stat_date` date NOT NULL COMMENT '统计日期',
  `stat_type` varchar(20) NOT NULL COMMENT '统计类型：daily-日统计，monthly-月统计，yearly-年统计',
  `scenic_id` bigint DEFAULT NULL COMMENT '景区ID（为空表示全站统计）',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `visit_count` int DEFAULT '0' COMMENT '访问次数',
  `user_count` int DEFAULT '0' COMMENT '用户数',
  `order_count` int DEFAULT '0' COMMENT '订单数',
  `order_amount` decimal(15,2) DEFAULT '0.00' COMMENT '订单金额',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stat_date_type_scenic` (`stat_date`, `stat_type`, `scenic_id`),
  KEY `idx_stat_date` (`stat_date`),
  KEY `idx_stat_type` (`stat_type`),
  KEY `idx_scenic_id` (`scenic_id`),
  KEY `idx_province_city` (`province`, `city`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据统计表';

-- 插入初始数据
-- 插入景区分类
INSERT INTO `scenic_category` (`name`, `level`, `description`) VALUES
('自然风光', '5A', '以自然景观为主的5A级景区'),
('历史文化', '5A', '以历史文化为主的5A级景区'),
('主题公园', '4A', '以主题娱乐为主的4A级景区'),
('博物馆', '4A', '以文化展示为主的4A级景区'),
('自然公园', '3A', '以自然景观为主的3A级景区');

-- 插入示例景区数据
INSERT INTO `scenic` (`name`, `category_id`, `province`, `city`, `address`, `longitude`, `latitude`, `phone`, `open_time`, `description`, `facilities`, `images`, `rating`, `visit_count`) VALUES
('故宫博物院', 2, '北京', '北京市', '北京市东城区景山前街4号', 116.397128, 39.916527, '010-85007421', '08:30-17:00', '明清两朝的皇家宫殿，世界文化遗产', '["停车场", "餐厅", "商店", "导游服务"]', '["https://example.com/gugong1.jpg", "https://example.com/gugong2.jpg"]', 4.8, 10000),
('天安门广场', 2, '北京', '北京市', '北京市东城区东长安街', 116.391296, 39.904200, '010-65131100', '05:00-22:00', '中华人民共和国的象征，世界最大的城市广场', '["停车场", "安检", "导游服务"]', '["https://example.com/tiananmen1.jpg"]', 4.7, 8000),
('颐和园', 2, '北京', '北京市', '北京市海淀区新建宫门路19号', 116.275108, 39.999982, '010-62881144', '06:30-18:00', '中国古典园林之首，皇家园林博物馆', '["停车场", "餐厅", "游船", "导游服务"]', '["https://example.com/yiheyuan1.jpg"]', 4.6, 6000),
('西湖风景名胜区', 1, '浙江', '杭州市', '浙江省杭州市西湖区龙井路1号', 120.155070, 30.274084, '0571-87179603', '全天开放', '中国著名的风景名胜区，世界文化遗产', '["停车场", "游船", "自行车租赁", "导游服务"]', '["https://example.com/xihu1.jpg"]', 4.9, 12000),
('黄山风景区', 1, '安徽', '黄山市', '安徽省黄山市黄山区汤口镇', 118.168000, 30.132000, '0559-5561111', '06:00-17:30', '中国著名的山岳风景区，世界自然文化遗产', '["索道", "酒店", "餐厅", "导游服务"]', '["https://example.com/huangshan1.jpg"]', 4.8, 9000);

-- 插入门票数据
INSERT INTO `ticket` (`scenic_id`, `name`, `type`, `price`, `original_price`, `description`, `valid_days`) VALUES
(1, '故宫博物院成人票', 'adult', 60.00, 60.00, '故宫博物院成人门票，包含所有开放区域', 1),
(1, '故宫博物院学生票', 'student', 30.00, 30.00, '故宫博物院学生门票，需持有效学生证', 1),
(2, '天安门广场参观票', 'adult', 0.00, 0.00, '天安门广场免费参观', 1),
(3, '颐和园成人票', 'adult', 30.00, 30.00, '颐和园成人门票', 1),
(3, '颐和园联票', 'adult', 60.00, 60.00, '颐和园门票+园中园门票', 1),
(4, '西湖游船票', 'adult', 55.00, 55.00, '西湖游船观光票', 1),
(5, '黄山风景区门票', 'adult', 190.00, 190.00, '黄山风景区成人门票', 1),
(5, '黄山索道票', 'adult', 80.00, 80.00, '黄山索道单程票', 1);

-- 插入测试用户
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `nickname`, `gender`) VALUES
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'test@example.com', '13800138000', '测试用户', 1),
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'admin@example.com', '13800138001', '管理员', 1);
