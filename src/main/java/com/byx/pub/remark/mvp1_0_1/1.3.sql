ALTER TABLE `byx`.`orders`
MODIFY COLUMN `status` int(11) NOT NULL DEFAULT 1 COMMENT '订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成(完全支付、手动完成 = 待结算)，5->已结算)',
MODIFY COLUMN `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '订单完成时间',
ADD COLUMN `refund_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '退款状态(0->无退款，1->退款中，2->完成退款)',
ADD COLUMN `refund_sn` varchar(255) NULL DEFAULT '' COMMENT  '退款单号(逗号隔开)',
ADD COLUMN `refund_time` datetime(0) NULL DEFAULT NULL COMMENT '退款时间(最后一次)',
ADD COLUMN `refund_amount` decimal(11, 2) NOT NULL DEFAULT 0.0 COMMENT '累计退款金额',
ADD COLUMN `refund_type` tinyint(2) NULL DEFAULT 0 COMMENT '退款类型(1：部分退款，2：全额退款)',
ADD COLUMN `pull_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '拉新人id',
ADD COLUMN `facilitate_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '促成人id';

ALTER TABLE `byx`.`pay_record`
ADD COLUMN `refund_status` tinyint(4) NULL DEFAULT 1 COMMENT '退款状态：1->未退款，2->部分退款，3->全额退款',
ADD COLUMN `refund_amount` decimal(11, 2) NULL DEFAULT 0 COMMENT '退款金额';

ALTER TABLE `byx`.`admin_card`
ADD COLUMN `nick_name` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称' AFTER `short_name`;

ALTER TABLE `byx`.`hot_admin`
CHANGE COLUMN `admin_id` `card_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名片id';

ALTER TABLE `byx`.`card_contact`
MODIFY COLUMN `contact_type` int(5) NOT NULL DEFAULT 0 COMMENT '联系方式类型(1：抖音，2：手机，3：企业微信，4：微信，5：小红书，6：微信视频号，7：哔哩哔哩)';

ALTER TABLE `byx`.`settlement_rules_range`
ADD COLUMN `settlement_type` int(2) NOT NULL DEFAULT 1 COMMENT '结算类型(1：拉新，2：促成，3：其他)',
ADD COLUMN `pull_type` int(2) NOT NULL DEFAULT 1 COMMENT '拉新类型(1：全部，2：名片拉新)',
DROP COLUMN `facilitate_rate`,
CHANGE COLUMN `pull_rate` `share_rate` decimal(8, 2) NOT NULL DEFAULT 0.00 COMMENT '分成比例';

ALTER TABLE `byx`.`order_detail`
ADD COLUMN `settlement_rule_id` varchar(32) NOT NULL DEFAULT '' COMMENT '结算规则id(下单那刻商品关联规则id)';

ALTER TABLE `byx`.`admin_card`
ADD COLUMN `user_img` varchar(255) NOT NULL DEFAULT '' COMMENT '微信授权头像' AFTER `service_times`;

CREATE TABLE `byx`.`refunds_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'id',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'SYSTEM' COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `order_sn` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
  `refund_sn` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '退款单号(微信)',
  `refund_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '退款流水号',
  `pay_record_id` varchar(32) NOT NULL DEFAULT '' COMMENT '支付记录id',
  `mchid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '商户号',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户ID',
  `refund_type` int(4) NULL DEFAULT 1 COMMENT '退款方式，1：微信，2：支付宝',
  `channel` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'ORIGINAL' COMMENT '退款渠道,ORIGINAL：原路退款,BALANCE：退回到余额',
  `amount` decimal(10, 2) NOT NULL COMMENT '原支付金额',
  `refund_amount` decimal(10, 2) NOT NULL COMMENT '退款金额',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '退款状态，1：退款处理中,2：退款成功,3：退款异常,4:退款关闭',
  `refunds_status` tinyint(4) NULL DEFAULT 0 COMMENT '请求状态，1：成功，其他：失败',
  `success_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `user_received_account` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '退款入账账户',
  `request_body` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求数据',
  `response_body` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '返回数据',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx-refund_sn`(`refund_sn`) USING BTREE,
  INDEX `idx-refund_id`(`refund_id`) USING BTREE,
  INDEX `idx-pay-id`(`pay_record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款记录表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`card_likes_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效,false:删除)',
  `card_id` varchar(32) NOT NULL DEFAULT '' COMMENT '名片id',
  `likes_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '点赞人id',
  `likes_name` varchar(50) NOT NULL DEFAULT '' COMMENT '点赞人昵称',
  `likes_img` varchar(255) NOT NULL DEFAULT '' COMMENT '点赞人头像',
  `liked_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '被赞人id',
  `liked_name` varchar(50) NOT NULL DEFAULT '' COMMENT '被赞人昵称',
  `liked_img` varchar(255) NOT NULL DEFAULT '' COMMENT '被赞人头像',
  PRIMARY KEY (`id`),
  INDEX `idx-likes-uid`(`likes_uid`) USING BTREE COMMENT '点赞人id索引',
  INDEX `idx-liked-uid`(`liked_uid`) USING BTREE COMMENT '被赞人id索引',
  INDEX `idx-card-id`(`card_id`) USING BTREE COMMENT '名片id索引'
) COMMENT = '名片点赞记录表';