CREATE TABLE `byx`.`settlement_detail`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间(出单时间)',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `main_id` varchar(32) NOT NULL DEFAULT '' COMMENT '账单id',
  `business_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家id',
  `business_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家名称',
  `order_sn` varchar(50) NOT NULL COMMENT '订单号',
  `order_amount` decimal(11, 2) NOT NULL DEFAULT 0.0 COMMENT '订单金额(实收)',
  `product_id` varchar(32) NOT NULL DEFAULT '' COMMENT '产品id',
  `product_name` varchar(100) NOT NULL DEFAULT '' COMMENT '产品名称',
  `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '用户id',
  `nick_name` varchar(100) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `settler_admin_id` varchar(32) NOT NULL DEFAULT '' COMMENT '结算人后台id',
  `settler_user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '结算人用户id',
  `settler_admin_name` varchar(50) NOT NULL DEFAULT '' COMMENT '结算人后台名称',
  `settler_user_name` varchar(50) NOT NULL DEFAULT '' COMMENT '结算人用户名称',
  `share_type` int(2) NOT NULL DEFAULT 1 COMMENT '分成类型(1：拉新，2：促成，3：其他)',
  `share_rate` decimal(8, 2) NOT NULL DEFAULT 0.00 COMMENT '分成比例',
  `settlement_amount` decimal(11, 2) NOT NULL DEFAULT 0.0 COMMENT '结算金额',
  `settlement_date` datetime(0) NULL DEFAULT NULL COMMENT '结算时间',
  `settlement_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '结算状态(false：待结算，true：已结算)',
  `settlement_id` varchar(32) NOT NULL DEFAULT '' COMMENT '结算id',
  `range_id` varchar(32) NOT NULL DEFAULT '' COMMENT '范围id',
  PRIMARY KEY (`id`),
  INDEX `idx-main-id`(`main_id`) USING BTREE COMMENT '主表id索引',
  INDEX `idx-sj-id`(`business_id`) USING BTREE COMMENT '商家id索引',
  INDEX `idx-product-id`(`product_id`) USING BTREE COMMENT '产品id索引',
  INDEX `idx-settler-aid`(`settler_admin_id`) USING BTREE COMMENT '结算人aid索引',
  INDEX `idx-settler-uid`(`settler_user_id`) USING BTREE COMMENT '结算人uid索引',
  INDEX `idx-settment-id`(`settlement_id`) USING BTREE COMMENT '结算主表id',
  INDEX `idx-ctime`(`create_time`) USING BTREE COMMENT '出单时间索引'
) COMMENT = '结算明细表';

CREATE TABLE `byx`.`settlement_main`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间(出单时间)',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `business_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家id',
  `business_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家名称',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品id',
  `product_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '产品名称',
  `income_amount` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '应收金额',
  `settlement_amount` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '结算金额',
  `settler_num` int(8) NOT NULL DEFAULT 0 COMMENT '结算人数',
  `order_num` int(8) NOT NULL DEFAULT 0 COMMENT '结算订单数',
  `settlement_date` datetime(0) NULL DEFAULT NULL COMMENT '结算时间',
  `settlement_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '结算状态(false：待结算，true：已结算)',
  `bill_date` varchar(50) NOT NULL DEFAULT '' COMMENT '账期(如：2023-09-01--2023-09-20)',
  PRIMARY KEY (`id`),
  INDEX `idx-pid`(`product_id`) USING BTREE COMMENT '产品id索引',
  INDEX `idx-bid`(`business_id`) USING BTREE COMMENT '商家id索引',
  INDEX `idx-ctime`(`create_time`) USING BTREE COMMENT '出单时间索引'
) COMMENT = '结算主表';

ALTER TABLE `byx`.`order_detail`
DROP COLUMN `settlement_rule_id`;


ALTER TABLE `byx`.`settlement_rules_range`
MODIFY COLUMN `target_id` varchar(32) NOT NULL DEFAULT '' COMMENT '目标id( 角色id 或 个人uid )';