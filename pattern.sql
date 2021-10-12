/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50553
Source Host           : 127.0.0.1:3306
Source Database       : sinsim-iot-master

Target Server Type    : MYSQL
Target Server Version : 50553
File Encoding         : 65001

Date: 2021-10-12 14:54:38
*/

SET FOREIGN_KEY_CHECKS=0;

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
