 CREATE DATABASE `byx` DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

CREATE TABLE `byx`.`admin`  (
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'SYSTEM' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `user_status` tinyint(1) NULL DEFAULT 0 COMMENT '用户状态',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '账户(手机号)',
  `user_password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录ip',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'openid',
  `union_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'unionId',
  `session_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'session_key',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '微信昵称',
  `user_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `gender` int(2) NULL DEFAULT 0 COMMENT '性别(0 未知 1 男 2女)',
  `true_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '真实姓名',
  `role_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '角色Id(1:管理员、2:咨询师)',
  `data_status` tinyint(1) NULL DEFAULT 1 COMMENT '数据状态(1->有效、0->无效)',
  PRIMARY KEY (`admin_id`) USING BTREE,
  INDEX `index_open_id`(`open_id`) USING BTREE COMMENT 'openId索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理后台用户表' ROW_FORMAT = Dynamic;

CREATE TABLE `byx`.`admin_month_bill`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效，false:无效)',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '咨询师id',
  `bill_date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '账期(2023-01)',
  `order_amount` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总额(收到支付金额)',
  `real_income` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '实收金额(抽佣后)',
  `settlement_ratio` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '结算比例',
  `commission` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '平台佣金',
  `order_num` int(8) NOT NULL DEFAULT 0 COMMENT '订单数',
  `customer_num` int(8) NOT NULL DEFAULT 0 COMMENT '获客数',
  `settlement_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '结算状态(true:已结算，false:未结算)',
  `settler` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '结算人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_admin_id`(`admin_id`) USING BTREE COMMENT '咨询师id索引',
  INDEX `index_bill_date`(`bill_date`) USING BTREE COMMENT ' 账期索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '咨询师月结表' ROW_FORMAT = Dynamic;

CREATE TABLE `byx`.`admin_product`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品id',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '咨询师Id',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(1:有效,0:无效)',
  `shelves_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '上下架状态(true:上架,false:下架)',
  `sale_num` int(10) NOT NULL DEFAULT 0 COMMENT '售卖次数',
  `product_source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品来源(PRODUCT_BASE:总部商品,PRODUCT_ADMIN:咨询师商品)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_admin_id`(`admin_id`) USING BTREE COMMENT '咨询师id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '咨询师商品对应店' ROW_FORMAT = Dynamic;

CREATE TABLE `byx`.`admin_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'id',
  `creator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `updator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'SYSTEM' COMMENT '更新者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '名称',
  `description` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
  `admin_count` int(11) NULL DEFAULT 0 COMMENT '后台用户数量',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '启用状态：0->禁用；1->启用',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序(越大越前)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`admin_user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SYSTEM' COMMENT '更新者',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '最近消费时间',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '咨询师id',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户id',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '微信昵称',
  `user_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户头像',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '手机号',
  `total_amount` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '累计消费金额',
  `total_order_num` int(8) NOT NULL DEFAULT 0 COMMENT '累计订单数',
  `user_create_time` datetime(0) NOT NULL COMMENT '用户建号时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_admin_id`(`admin_id`) USING BTREE COMMENT '咨询师索引',
  INDEX `index_user_id`(`user_id`) USING BTREE COMMENT '用户索引',
  INDEX `index_mobile`(`mobile`) USING BTREE COMMENT '手机号'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '咨询师会员表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`hot_info`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效，false:无效)',
  `title_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章标题',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章图片',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章链接',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_update_time`(`update_time`) USING BTREE COMMENT '修改时间索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '热播资讯表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`order_detail`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `order_sn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品id',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '商品价格',
  `quantity` int(11) NOT NULL DEFAULT 0 COMMENT '商品数量',
  `img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图片路径',
  `protocol` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '协议图片',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_order_sn`(`order_sn`) USING BTREE COMMENT '订单号索引',
  INDEX `index_product_id`(`product_id`) USING BTREE COMMENT '商品id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单明细表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`orders`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'SYSTEM' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `order_sn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '单号',
  `status` int(11) NOT NULL DEFAULT 1 COMMENT '订单状态(0->已取消，1->待支付，2->部分支付，3->已支付，4->已完成)',
  `pay_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '支付状态(0->未支付，1->部分支付，2->已支付)',
  `payment` int(11) NOT NULL DEFAULT 1 COMMENT '支付方式(1->微信支付，2->支付宝)',
  `full_pay` tinyint(1) NOT NULL DEFAULT 1 COMMENT '支付类型:true 全额支付，false 分期支付',
  `order_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单金额',
  `paid_amount` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '已支付金额',
  `surplus_amount` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '剩余支付金额',
  `this_payment` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '本次支付金额',
  `pay_sn` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '完成支付流水号',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '完成支付时间',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '咨询师id',
  `admin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '咨询师名称',
  `admin_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '咨询师手机',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人',
  `user_mobile` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单手机',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '下单人openid',
  `first_pay_time` datetime(0) NULL DEFAULT NULL COMMENT '第一次支付时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_order_sn`(`order_sn`) USING BTREE COMMENT '订单号id索引',
  INDEX `index_user_id`(`user_id`) USING BTREE COMMENT '下单人id索引',
  INDEX `index_admin_id`(`admin_id`) USING BTREE COMMENT '咨询师id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单主表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`pay_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '支付记录ID',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `order_sn` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '订单号',
  `out_trade_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商家订单号(微信规范32位)',
  `pay_sn` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '支付流水号(微信规范32位)',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户ID',
  `openid` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'openid',
  `pay_type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '支付类型:1->微信',
  `trade_type` tinyint(2) NOT NULL DEFAULT 1 COMMENT '交易类型:1->JSAP',
  `description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品描述',
  `amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '支付金额',
  `discount_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
  `products_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单金额',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '交易状态:1->待支付，2->已支付，3->转入退款，4->已关闭，5->已撤销',
  `place_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '下单状态:1->成功，0->：失败',
  `success_time` datetime(0) NULL DEFAULT NULL COMMENT '完成支付时间',
  `attch` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '附加信息',
  `request_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求数据',
  `response_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回数据',
  `notify_body` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '支付通知数据',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_order_sn`(`order_sn`) USING BTREE,
  INDEX `index_out_trade_no`(`out_trade_no`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付记录表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`product`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `updator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新者',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品名称',
  `product_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品编码',
  `price` decimal(11, 2) NOT NULL DEFAULT 0.00 COMMENT '商品价格',
  `img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品图片分号分隔第一张为首图',
  `all_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '全部状态(true:全部,false:部分)',
  `shelves_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '上下架状态(true:上架,false:下架)',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效,false:删除)',
  `product_source` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '商品来源(PRODUCT_BASE:总部商品,PRODUCT_ADMIN:咨询师商品)',
  `audit_status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '审核状态(0:待审核,1:审核通过,2审核不通过)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_product_name`(`product_name`) USING BTREE COMMENT '名称索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`temp_admin`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'SYSTEM' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'openid',
  `band_status` tinyint(1) NULL DEFAULT 0 COMMENT '用户绑定: 1绑定 0未绑定',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '微信昵称',
  `user_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `gender` int(2) NULL DEFAULT 0 COMMENT '性别(0 未知 1 男 2女)',
  `union_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'unionId',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_openid`(`open_id`) USING BTREE COMMENT 'openid索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '临时用户表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`user`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'SYSTEM' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'SYSTEM' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `user_status` tinyint(1) NULL DEFAULT 1 COMMENT '用户状态',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'openid',
  `union_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'unionId',
  `session_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'session_key',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '微信昵称',
  `user_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户头像',
  `gender` int(2) NULL DEFAULT 0 COMMENT '性别(0 未知 1 男 2女)',
  `role_type` tinyint(2) NULL DEFAULT 1 COMMENT '角色类型(1:用户、2:咨询师)',
  `admin_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '咨询师id',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `data_status` tinyint(1) NULL DEFAULT 1 COMMENT '数据状态(1->有效、0->无效)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_open_id`(`open_id`) USING BTREE COMMENT 'openid索引',
  INDEX `index_mobile`(`mobile`) USING BTREE COMMENT '手机索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '客户表' ROW_FORMAT = Dynamic;


CREATE TABLE `byx`.`login_token`(
  `id` varchar(32) NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `login_id` varchar(32) NOT NULL DEFAULT '' COMMENT '登录人id',
  `token_str` varchar(100) NOT NULL DEFAULT '' COMMENT '登录人token',
  `res_str` varchar(100) NOT NULL DEFAULT '' COMMENT '返回前端的token',
  PRIMARY KEY (`id`),
  INDEX `index_login_id`(`login_id`) USING BTREE COMMENT '登录人id索引'
) COMMENT = '登录token表';