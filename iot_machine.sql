/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50553
Source Host           : 127.0.0.1:3306
Source Database       : sinsim-iot-master

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2021-10-21 11:51:56
*/

SET FOREIGN_KEY_CHECKS=0;

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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb3;
