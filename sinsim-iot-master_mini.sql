/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50553
Source Host           : 127.0.0.1:3306
Source Database       : sinsim-iot-master

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2022-01-19 16:04:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account_info
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info` (
  `account_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '账号id',
  `user_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名称',
  `account` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '账号名称',
  `password` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '账号密码',
  `park_id` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '所属园区',
  `role_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色id',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='账号信息表';

-- ----------------------------
-- Table structure for attendance_config
-- ----------------------------
DROP TABLE IF EXISTS `attendance_config`;
CREATE TABLE `attendance_config` (
  `id` varchar(100) NOT NULL COMMENT '唯一标识',
  `name` varchar(255) DEFAULT NULL COMMENT '规则名称',
  `start_time` varchar(255) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(255) DEFAULT NULL COMMENT '结束时间',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for attendance_record
-- ----------------------------
DROP TABLE IF EXISTS `attendance_record`;
CREATE TABLE `attendance_record` (
  `record_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '记录id',
  `staff_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '员工姓名',
  `staff_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '员工id',
  `staff_number` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '员工工号',
  `staff_park_name` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '所属园区名称',
  `staff_park_id` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '所属园区id',
  `staff_tag_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '员工标签名称',
  `staff_tag_id` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '员工标签id',
  `sign_day` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '员工签到日期',
  `sign_device_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '上班打卡设备',
  `sign_out_device_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '下班打卡设备',
  `sign_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '上班签到时间',
  `sign_out_time` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '下班签到时间',
  `status` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '考勤状态',
  `type` int(10) DEFAULT '0' COMMENT '签到类型，0：刷脸，1：补签',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for departing_staff
-- ----------------------------
DROP TABLE IF EXISTS `departing_staff`;
CREATE TABLE `departing_staff` (
  `staff_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '员工id',
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '姓名',
  `staff_number` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '工号',
  `card_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '卡号',
  `face_ids` text COLLATE utf8_unicode_ci COMMENT '人像id',
  `tag_ids` text COLLATE utf8_unicode_ci NOT NULL COMMENT '人员标签',
  `contact` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `park_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '所属园区',
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `entry_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '入职时间',
  `birthday` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '生日',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `nickname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信昵称',
  `open_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信open id',
  `wechat_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信号',
  `photo_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信头像',
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'Tk123!' COMMENT '密码',
  `is_block` tinyint(1) DEFAULT '0' COMMENT '是否拉人黑名单',
  `clause_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '已阅读的条款id',
  `meta` longtext COLLATE utf8_unicode_ci COMMENT '扩展字段',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`staff_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='员工信息表';

-- ----------------------------
-- Table structure for iot_machine
-- ----------------------------
DROP TABLE IF EXISTS `iot_machine`;
CREATE TABLE `iot_machine` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'iot开头的都是绣花机物联网项目',
  `nameplate` varchar(255) DEFAULT NULL,
  `machine_model_info` varchar(255) DEFAULT NULL COMMENT '机型信息',
  `uptime` varchar(255) DEFAULT NULL COMMENT '已开机的时间',
  `working_time` varchar(255) DEFAULT NULL COMMENT '刺绣（工作）时间',
  `nonworking_time` varchar(255) DEFAULT NULL COMMENT '停机时间',
  `line_broken_number` varchar(50) DEFAULT NULL COMMENT '断线次数',
  `line_broken_average_time` varchar(100) DEFAULT NULL,
  `product_total_number` varchar(50) DEFAULT NULL COMMENT '工件总数',
  `power_on_times` varchar(10) DEFAULT NULL COMMENT '开机次数',
  `needle_total_number` varchar(50) DEFAULT NULL COMMENT '累计针数',
  `user` varchar(50) DEFAULT NULL COMMENT '该机器记录信息的创建账号',
  `pattern` varchar(1000) DEFAULT NULL COMMENT '该机器的花样(考虑到不同的用户，可能购买不同花样）',
  `last_status` varchar(10) DEFAULT NULL COMMENT '最后报上来的状态（运行中、故障、空闲），这个状态表明了机器的最后状态',
  `machine_status` varchar(255) DEFAULT NULL COMMENT '机器真实状态，是在last_status基础上更新是否离线',
  `created_time` datetime DEFAULT NULL COMMENT '该机器记录的创建时间',
  `updated_time` datetime DEFAULT NULL COMMENT '该机器记录的最后更新时间, 可用于判断是否离线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for log_record
-- ----------------------------
DROP TABLE IF EXISTS `log_record`;
CREATE TABLE `log_record` (
  `log_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '日志id',
  `module` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '模块名称',
  `operator` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '触发者',
  `log_type` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '日志类型',
  `message` text COLLATE utf8_unicode_ci NOT NULL COMMENT '详细信息',
  `record_time` bigint(20) NOT NULL COMMENT '记录时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='操作日志表';

-- ----------------------------
-- Table structure for malfunction
-- ----------------------------
DROP TABLE IF EXISTS `malfunction`;
CREATE TABLE `malfunction` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `nameplate` varchar(50) DEFAULT NULL COMMENT '发生故障的机器的铭牌号',
  `machine_stop_time` varchar(50) DEFAULT NULL COMMENT '机器的本次故障的停车时间（在恢复工作时更新该值）',
  `malfunction_reason` varchar(500) DEFAULT NULL COMMENT '故障的原因',
  `created_time` datetime DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for park_info
-- ----------------------------
DROP TABLE IF EXISTS `park_info`;
CREATE TABLE `park_info` (
  `park_id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '园区id',
  `park_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '园区名称',
  `unit` text COLLATE utf8_bin COMMENT '单位',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`park_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for pattern
-- ----------------------------
DROP TABLE IF EXISTS `pattern`;
CREATE TABLE `pattern` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '花样',
  `user` varchar(50) DEFAULT NULL COMMENT '花样的所有者',
  `pattern_name` varchar(50) DEFAULT NULL COMMENT '花样名称',
  `pattern_number` varchar(50) DEFAULT NULL COMMENT '“花样数目”',
  `needles_total` varchar(50) DEFAULT NULL COMMENT '总针数',
  `embroidery_time` varchar(50) DEFAULT NULL COMMENT '刺绣时间',
  `parts_total_number` int(10) DEFAULT NULL COMMENT '工件数量',
  `machine_model` varchar(1000) DEFAULT NULL COMMENT '花样适配哪些机型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
  `role_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色id',
  `role_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '角色名称',
  `scope` text COLLATE utf8_unicode_ci COMMENT '权限范围 对应功能列表路径',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述 角色权限说明',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='角色信息表';

-- ----------------------------
-- Table structure for staff_info
-- ----------------------------
DROP TABLE IF EXISTS `staff_info`;
CREATE TABLE `staff_info` (
  `staff_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '员工id',
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '姓名',
  `staff_number` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '工号',
  `card_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '卡号',
  `face_ids` text COLLATE utf8_unicode_ci COMMENT '人像id',
  `tag_ids` text COLLATE utf8_unicode_ci NOT NULL COMMENT '人员标签',
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT 'Taikang123!' COMMENT '密码',
  `contact` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系方式',
  `park_id` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '所属园区',
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `entry_time` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '入职时间',
  `birthday` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '生日',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `nickname` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信昵称',
  `open_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信open id',
  `wechat_number` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信号',
  `photo_address` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '微信头像',
  `is_block` tinyint(1) DEFAULT '0' COMMENT '是否拉人黑名单',
  `clause_id` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '已阅读的条款id',
  `meta` longtext COLLATE utf8_unicode_ci COMMENT '扩展字段',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`staff_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='员工信息表';

-- ----------------------------
-- Table structure for staff_reissue_record
-- ----------------------------
DROP TABLE IF EXISTS `staff_reissue_record`;
CREATE TABLE `staff_reissue_record` (
  `id` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '记录id',
  `staff_id` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '员工id',
  `staff_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '员工姓名',
  `staff_num` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '员工工号',
  `staff_park_id` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '园区id',
  `staff_park_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '园区姓名',
  `staff_tag_id` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '单位id',
  `staff_tag_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '单位名称',
  `sign_time` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '签到时间',
  `sign_out_time` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '签退时间',
  `status` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '状态',
  `remark` text COLLATE utf8_bin COMMENT '备注',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for tag_info
-- ----------------------------
DROP TABLE IF EXISTS `tag_info`;
CREATE TABLE `tag_info` (
  `tag_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '标签id',
  `tag_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '标签名称',
  `tag_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标签类型：STAFF、VISITOR',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` bigint(20) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='标签表';
