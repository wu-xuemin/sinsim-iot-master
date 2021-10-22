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
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'iot开头的都是绣花机物联网项目',
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
  `machine_status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '机器状态-【运行中】：如果能定时收到信息，且针数增加，则判断为运行中\n机器状态-【空   闲】：如果能定时收到信息，且针数不变，则判断为空闲\n机器状态-【故   障】：收到的最后信息为“故障”\n机器状态-【离   线】：在固定时内没收到信息，则判断为离线。',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb3;
