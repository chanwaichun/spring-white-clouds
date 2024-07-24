ALTER TABLE `byx`.`admin`
ADD COLUMN `xhs_seller_id` varchar(32) NOT NULL DEFAULT '' COMMENT '小红书店铺id',
ADD INDEX `idx-xhs-id`(`xhs_seller_id`) USING BTREE COMMENT '小红书店铺id索引';

ALTER TABLE `byx`.`orders`
ADD COLUMN `xhs_order_id` varchar(32) NOT NULL DEFAULT '' COMMENT '小红书订单id',
ADD COLUMN `xhs_address_id` varchar(50) NOT NULL DEFAULT '' COMMENT '小红书收货人id',
MODIFY COLUMN `shipping_address` varchar(500) NOT NULL DEFAULT '' COMMENT '收货地址',
ADD INDEX `idx-xhs-oid`(`xhs_order_id`) USING BTREE COMMENT '小红书订单id索引';

ALTER TABLE `byx`.`business_user`
ADD COLUMN `xhs_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '小红书会员(0：否，1：是)';

ALTER TABLE `byx`.`business_user`
MODIFY COLUMN `user_type` int(5) NOT NULL DEFAULT 0 COMMENT '用户类型(0：支付订单，1：查看商品，2：查看名片，3：查看电子传单，4：小红书下单)';

ALTER TABLE `byx`.`user`
MODIFY COLUMN `shipping_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '收货地址';

ALTER TABLE `byx`.`product`
ADD COLUMN `xhs_sku_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '小红书状态 (true：是，false：否)';

CREATE TABLE `byx`.`xhs_auth_token`  (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `seller_id` varchar(32) NOT NULL DEFAULT '' COMMENT '店铺id',
  `seller_name` varchar(100) NOT NULL DEFAULT '' COMMENT '店铺名称',
  `access_token` varchar(150) NOT NULL DEFAULT '' COMMENT 'token',
  `token_expires` varchar(50) NOT NULL DEFAULT '' COMMENT '过期时间戳',
  `refresh_token` varchar(150) NOT NULL DEFAULT '' COMMENT '刷新token',
  `refresh_expires` varchar(50) NOT NULL DEFAULT '' COMMENT '刷新token过期时间戳',
  PRIMARY KEY (`id`),
  INDEX `idx_seller_id`(`seller_id`) USING BTREE COMMENT '店铺索引',
  INDEX `idx_token_expires`(`token_expires`) USING BTREE COMMENT '过期时间戳索引'
) COMMENT = '小红书授权token表';

CREATE TABLE `byx`.`xhs_admin_product`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `bid` varchar(32) NOT NULL DEFAULT '' COMMENT '商家id',
  `admin_id` varchar(32) NOT NULL DEFAULT '' COMMENT '咨询师id',
  `product_id` varchar(32) NOT NULL DEFAULT '' COMMENT '商品id',
  `xhs_sku_id` varchar(50) NOT NULL DEFAULT '' COMMENT '小红书skuId',
  `xhs_seller_id` varchar(50) NOT NULL DEFAULT '' COMMENT '小红书店铺id',
  PRIMARY KEY (`id`),
  INDEX `idx-xhs-sellerid`(`xhs_seller_id`) USING BTREE COMMENT '小红书店铺id索引',
  INDEX `idx-admin-id`(`admin_id`) USING BTREE COMMENT '咨询师id索引',
  INDEX `idx-bid`(`bid`) USING BTREE COMMENT '商家id索引'
) COMMENT = '咨询师推送小红书商品推送表';