CREATE TABLE `byx`.`smsg_auth_record`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `data_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '数据状态(true:有效,false:删除)',
  `admin_id` varchar(32) NOT NULL DEFAULT '' COMMENT '咨询师id',
  `template_id` varchar(255) NOT NULL DEFAULT '' COMMENT '模版id',
  `always_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '总是状态(true：是，false：否)',
  PRIMARY KEY (`id`),
  INDEX `index_admin_id`(`admin_id`) USING BTREE COMMENT '咨询师id索引',
  INDEX `index_template_id`(`template_id`) USING BTREE COMMENT '模板id索引'
) COMMENT = '订阅消息授权记录表';