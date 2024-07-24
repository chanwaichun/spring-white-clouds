CREATE TABLE `byx`.`flyer_relay`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效，false:无效)',
  `flyer_bid` varchar(32) NOT NULL DEFAULT '' COMMENT '传单商家id',
  `flyer_id` varchar(32) NOT NULL DEFAULT '' COMMENT '传单id',
  `flyer_name` varchar(50) NOT NULL DEFAULT '' COMMENT '传单名称',
  `share_rid` int(5) NOT NULL DEFAULT 6 COMMENT '分享人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)',
  `share_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '分享人uid',
  `share_name` varchar(20) NOT NULL DEFAULT '' COMMENT '分享人姓名',
  `relay_rid` int(5) NOT NULL DEFAULT 0 COMMENT '转发人角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)',
  `relay_bid` varchar(32) NOT NULL DEFAULT '' COMMENT '转发人商家id',
  `relay_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '转发人uid',
  `relay_name` varchar(32) NOT NULL DEFAULT '' COMMENT '转发人昵称',
  `supplier_rid` int(5) NOT NULL DEFAULT 0 COMMENT '供应商角色(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)',
  `supplier_bid` varchar(32) NOT NULL DEFAULT '' COMMENT '供应商商家id',
  `supplier_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '供应商uid',
  `supplier_name` varchar(32) NOT NULL DEFAULT '' COMMENT '供应商昵称',
  PRIMARY KEY (`id`),
  INDEX `idx-flyer-id`(`flyer_id`) USING BTREE COMMENT '传单id索引',
  INDEX `idx-share-id`(`share_uid`) USING BTREE COMMENT '分享人id索引',
  INDEX `idx-supplier-uid`(`supplier_uid`) USING BTREE COMMENT '供应商人id索引',
  INDEX `idx-relay-id`(`relay_uid`) USING BTREE COMMENT '转发人id索引'
) COMMENT = '传单转发表';

CREATE TABLE `byx`.`flyer_relay_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `relay_id` varchar(32) NOT NULL DEFAULT '' COMMENT '转发主键',
  `flyer_bid` varchar(32) NOT NULL DEFAULT '' COMMENT '传单商家id'
  `flyer_id` varchar(32) NOT NULL DEFAULT '' COMMENT '传单id',
  `relay_sj_name` varchar(100) NOT NULL DEFAULT '' COMMENT '转发商家名称',
  `flyer_name` varchar(50) NOT NULL DEFAULT '' COMMENT '传单名称',
  `relay_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '转发人uid',
  `relay_name` varchar(50) NOT NULL DEFAULT '' COMMENT '转发人昵称',
  `Invitee_uid` varchar(32) NOT NULL DEFAULT '' COMMENT '受邀人uid',
  `Invitee_name` varchar(50) NOT NULL DEFAULT ' ' COMMENT '受邀人昵称',
  `invitee_phone` varchar(20) NOT NULL DEFAULT '' COMMENT '受邀人手机',
  `deal_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '成交状态(true：成交，false：未成交)',
  `order_sn` varchar(32) NOT NULL DEFAULT '' COMMENT '成交订单号',
  `order_product` varchar(100) NOT NULL DEFAULT '' COMMENT '成交商品',
  `deal_time` varchar(30) NOT NULL DEFAULT '' COMMENT '成交时间',
  PRIMARY KEY (`id`),
  INDEX `idx-relay-id`(`relay_id`) USING BTREE COMMENT '转发id索引',
  INDEX `idx-flyer-id`(`flyer_id`) USING BTREE COMMENT '传单id索引',
  INDEX `idx-realy-uid`(`relay_uid`) USING BTREE COMMENT '转发人id索引'
) COMMENT = '转发邀请记录表';

ALTER TABLE `byx`.`flyer_info`
ADD COLUMN `jump_button_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '跳转按钮状态(true:开，false:关)' AFTER `page_content`,
ADD COLUMN `button_name` varchar(20) NOT NULL DEFAULT '' COMMENT '按钮名称' AFTER `jump_button_status`,
ADD COLUMN `button_jump_url` varchar(255) NOT NULL DEFAULT '' COMMENT '按钮跳转url' AFTER `button_name`;

ALTER TABLE `byx`.`admin`
MODIFY COLUMN `role_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '角色Id(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人)' AFTER `true_name`;

UPDATE byx.admin_role SET description = '商家推荐官' where id = '6';
UPDATE byx.admin_role SET description = '商家助教' where id = '7';

ALTER TABLE `byx`.`user`
MODIFY COLUMN `role_id` int(5) NULL DEFAULT 0 COMMENT '角色类型(1：平台管理员，2：平台运营，3：商家管理员，4：商家导师，5：商家老师，6：商家推荐官，7：商家助教，8：合伙人，0：消费者)' AFTER `gender`;

