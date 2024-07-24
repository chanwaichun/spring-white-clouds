ALTER TABLE `byx`.`tag`
ADD COLUMN `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
ADD INDEX `idx-tag-name`(`tag_name`) USING BTREE COMMENT '标签名称索引';

CREATE TABLE `byx`.`flyer_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效，false:无效)',
  `flyer_name` varchar(50) NOT NULL DEFAULT '' COMMENT '传单名称',
  `business_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家id',
  `business_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家商家简称',
  `share_title` varchar(20) NOT NULL DEFAULT '' COMMENT '分享文案',
  `share_img` varchar(100) NOT NULL DEFAULT '' COMMENT '分享图片',
  `page_content` mediumtext NULL COMMENT '页面内容',
  PRIMARY KEY (`id`),
  INDEX `idx-bId`(`business_id`) USING BTREE COMMENT '商家id索引'
) COMMENT = '电子传单表';

CREATE TABLE `byx`.`flyer_share`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效，false:无效)',
  `flyer_id` varchar(32) NOT NULL DEFAULT '' COMMENT '传单id',
  `flyer_name` varchar(50) NOT NULL DEFAULT '' COMMENT '传单名称',
  `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '分享人uid(小程序id)',
  `admin_id` varchar(32) NOT NULL DEFAULT '' COMMENT '分享人id(后台id)',
  `true_name` varchar(20) NOT NULL DEFAULT '' COMMENT '分享人真实姓名',
  `img` varchar(255) NOT NULL DEFAULT '' COMMENT '分享人头像',
  `role_id` int(5) NOT NULL DEFAULT 6 COMMENT '分享人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)',
  `share_type` int(2) NOT NULL DEFAULT 1 COMMENT '分享类型(1：小程序卡片，2：H5页面，3：二维码)',
  PRIMARY KEY (`id`),
  INDEX `idx-admin-id`(`admin_id`) USING BTREE COMMENT '分享人id索引',
  INDEX `idx-flyer-id`(`flyer_id`) USING BTREE COMMENT '传单id索引'
) COMMENT = '传单分享主表';

CREATE TABLE `byx`.`flyer_pull_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `share_id` varchar(32) NOT NULL DEFAULT '' COMMENT '分享主键',
  `flyer_id` varchar(32) NOT NULL DEFAULT '' COMMENT '传单id',
  `flyer_name` varchar(50) NOT NULL DEFAULT '' COMMENT '传单名称',
  `share_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '分享人uid',
  `share_name` varchar(50) NOT NULL DEFAULT '' COMMENT '分享人昵称',
  `Invitee_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '受邀人uid',
  `Invitee_name` varchar(50) NOT NULL DEFAULT '' COMMENT '受邀人昵称',
  `deal_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '成交状态(true：成交，false：未成交)',
  `business_id` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  INDEX `idx-share_id`(`share_id`) USING BTREE COMMENT '分享id索引',
  INDEX `idx-share-uid`(`share_uid`) USING BTREE COMMENT '分享用户id索引'
) COMMENT = '传单拉新记录表';


CREATE TABLE `byx`.`interactive_click_log`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `promotion_id` varchar(32) NOT NULL DEFAULT '' COMMENT '活动id',
  `promotion_type` int(2) NOT NULL DEFAULT 0 COMMENT '活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享)',
  `share_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '分享人uid',
  `click_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '点击人uid',
  PRIMARY KEY (`id`),
  INDEX `idx-share-uid-click_uid`(`share_uid`, `click_uid`) USING BTREE COMMENT '分享用户id索引'
) COMMENT = '互动点击日志表';