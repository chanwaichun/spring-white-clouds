ALTER TABLE `byx`.`admin_card`
ADD COLUMN `qr_code` varchar(100) NOT NULL DEFAULT '' COMMENT '名片二维码';
ALTER TABLE `byx`.`product`
MODIFY COLUMN `product_name` varchar(100) NOT NULL DEFAULT '' COMMENT '商品名称';

ALTER TABLE `byx`.`business_user`
MODIFY COLUMN `update_time` datetime(0) NULL DEFAULT NULL COMMENT '最近消费时间',
ADD COLUMN `user_type` int(5) NOT NULL DEFAULT 0 COMMENT '用户类型(0：支付订单，1：查看商品，2：查看名片，3：查看电子传单)';

CREATE TABLE `byx`.`member_group`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效,false:删除)',
  `business_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家id',
  `group_name` varchar(30) NOT NULL DEFAULT '' COMMENT '用户组名称',
  `update_status` int(2) NOT NULL DEFAULT 1 COMMENT '用户组更新状态(1：更新中，2：更新完成)',
  `member_num` int(8) NOT NULL DEFAULT 0 COMMENT '用户数',
  `rule_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '规则状态(true：启用，false：停用)',
  PRIMARY KEY (`id`),
  INDEX `idx-bid`(`business_id`) USING BTREE COMMENT '商家id索引'
) COMMENT = '客户组表';

CREATE TABLE `byx`.`member_group_rule`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `group_id` varchar(32) NOT NULL COMMENT '分组id',
  `rule_type` int(2) NOT NULL DEFAULT 1 COMMENT '用户行为(1：订单状态，2：电子传单，3：购买服务)',
  `rule_value` varchar(50) NOT NULL DEFAULT '' COMMENT '行为值(订单状态：0：取消，1：待支付，2：支付定金，4：已完成)，传单id，服务id',
  `rule_txt` varchar(100) NOT NULL DEFAULT '' COMMENT '行为文案(状态名称，传单名称，服务名称)',
  PRIMARY KEY (`id`),
  INDEX `idx-group-id`(`group_id`) USING BTREE COMMENT '分组id索引'
) COMMENT = '客户组表规则表';

CREATE TABLE `byx`.`member_group_relation`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `group_id` varchar(32) NOT NULL DEFAULT '' COMMENT '分组id',
  `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '用户id',
  `nick_name` varchar(100) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `user_img` varchar(255) NOT NULL DEFAULT '' COMMENT '用户头像',
  `in_type` int(2) NOT NULL DEFAULT 1 COMMENT '进组类型(1：规则，2：白名单)',
  PRIMARY KEY (`id`),
  INDEX `idx-uid`(`user_id`) USING BTREE COMMENT '用户id索引',
  INDEX `idx-group-id`(`group_id`) USING BTREE COMMENT '分组索引'
) COMMENT = '用户分组关联表';

CREATE TABLE `byx`.`member_group_blacklist`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `group_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分组id',
  `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '用户id',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户昵称',
  `user_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  PRIMARY KEY (`id`),
  INDEX `idx-uid`(`user_id`) USING BTREE COMMENT '用户id索引',
  INDEX `idx-group-id`(`group_id`) USING BTREE COMMENT '分组索引'
) COMMENT = '客户分组黑名单';


--------------------------------------------  以上已执行 ------------------------------------------------

ALTER TABLE `byx`.`card_poster`
MODIFY COLUMN `poster_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '广告位图',
ADD COLUMN `poster_type` int(2) NOT NULL DEFAULT 1 COMMENT '广告类型(1：广告商品，2：广告图)';

ALTER TABLE `byx`.`card_share_file`
ADD COLUMN `share_type` int(2) NOT NULL DEFAULT 1 COMMENT '分享类型(1：文档分享，2：微信文章，3：微信视频号，4：快手视频，5：抖音视频，6：小红书，7：B站)' AFTER `card_id`,
MODIFY COLUMN `file_name` varchar(60) NOT NULL DEFAULT '' COMMENT '分享名称(合集名称)' AFTER `card_id`,
MODIFY COLUMN `file_url` varchar(100) NOT NULL DEFAULT '' COMMENT '上传文件(图片)名称(文件名、图片名)、合集icon' AFTER `file_name`,
ADD COLUMN `search_name` varchar(20) NOT NULL DEFAULT '' COMMENT '搜索名' AFTER `suffix_name`,
ADD COLUMN `jump_url` varchar(255) NOT NULL DEFAULT '' COMMENT '跳转连接(文章连接、视频连接、视频号)、合集简介' AFTER `search_name`,
ADD COLUMN `folder_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '合集状态(true：是，false：否)' AFTER `jump_url`,
ADD COLUMN `folder_id` varchar(32) NOT NULL DEFAULT '' COMMENT '所属合集id' AFTER `folder_status`,
ADD INDEX `idx-folder-id`(`folder_id`) USING BTREE COMMENT '合集id索引',
COMMENT = '名片分享表';

ALTER TABLE `byx`.`interactive_click_log`
ADD COLUMN `bid` varchar(32) NOT NULL DEFAULT '' COMMENT '商家id',
MODIFY COLUMN `promotion_type` int(2) NOT NULL DEFAULT 0 COMMENT '活动类型(1：名片点赞、2：电子传单、3：名片分享、4：商品分享、5：创建订单、6：定金支付、7：名片浏览、8：服务浏览)';

ALTER TABLE `byx`.`orders`
ADD COLUMN `first_pay_money` decimal(11, 2) NOT NULL DEFAULT 0.0 COMMENT '首付金额' AFTER `first_pay_time`;

update byx.`user` set nick_name = CONCAT('微信用户',FLOOR(RAND() * 100000000)) where nick_name='';

------------------------------------ 以上已执行 ------------------------------------------------

ALTER TABLE `byx`.`product`
MODIFY COLUMN `service_num` int(9) NOT NULL DEFAULT 99999999 COMMENT '服务次数(服务人数、库存)';

update `byx`.`product` SET service_num = 99999999 where service_num=0;

ALTER TABLE `byx`.`card_share_file`
MODIFY COLUMN `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名称(文件中文名)' AFTER `card_id`,
ADD COLUMN `share_name` varchar(60) NULL DEFAULT '' COMMENT '分享名称',
ADD COLUMN `wx_video_sn` varchar(255) NOT NULL DEFAULT '' COMMENT '微信视频号id',
ADD COLUMN `video_id` varchar(255) NOT NULL DEFAULT '' COMMENT '视频id';
