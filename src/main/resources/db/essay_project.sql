/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : essay_project

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 08/05/2022 17:59:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `essay_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `root_comment_id` bigint(20) NOT NULL,
  `parent_comment_id` bigint(11) NULL DEFAULT NULL,
  `content` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `root_comment_id`(`root_comment_id`) USING BTREE,
  INDEX `parent_comment_id`(`parent_comment_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `comment_ibfk_1`(`essay_id`) USING BTREE,
  CONSTRAINT `comment_ibfk_4` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comment_ibfk_5` FOREIGN KEY (`root_comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comment_ibfk_6` FOREIGN KEY (`parent_comment_id`) REFERENCES `comment` (`comment_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `comment_ibfk_7` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------

-- ----------------------------
-- Table structure for direction
-- ----------------------------
DROP TABLE IF EXISTS `direction`;
CREATE TABLE `direction`  (
  `direction_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `classification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `group` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `branch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`direction_id`) USING BTREE,
  INDEX `direction_id`(`direction_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of direction
-- ----------------------------

-- ----------------------------
-- Table structure for essay
-- ----------------------------
DROP TABLE IF EXISTS `essay`;
CREATE TABLE `essay`  (
  `essay_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `conference` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `publish_date` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `essay_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `essay_type` enum('P','O','E','T','D') CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`essay_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `essay_id`(`essay_id`) USING BTREE,
  CONSTRAINT `essay_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of essay
-- ----------------------------

-- ----------------------------
-- Table structure for essay_direction
-- ----------------------------
DROP TABLE IF EXISTS `essay_direction`;
CREATE TABLE `essay_direction`  (
  `essay_id` bigint(20) NOT NULL,
  `direction_id` bigint(20) NOT NULL,
  PRIMARY KEY (`essay_id`, `direction_id`) USING BTREE,
  INDEX `direction_id`(`direction_id`) USING BTREE,
  CONSTRAINT `essay_direction_ibfk_3` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `essay_direction_ibfk_4` FOREIGN KEY (`direction_id`) REFERENCES `direction` (`direction_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of essay_direction
-- ----------------------------

-- ----------------------------
-- Table structure for essay_reference
-- ----------------------------
DROP TABLE IF EXISTS `essay_reference`;
CREATE TABLE `essay_reference`  (
  `essay_id` bigint(20) NOT NULL,
  `reference_id` bigint(20) NOT NULL,
  PRIMARY KEY (`essay_id`, `reference_id`) USING BTREE,
  INDEX `reference_id`(`reference_id`) USING BTREE,
  CONSTRAINT `essay_reference_ibfk_1` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `essay_reference_ibfk_2` FOREIGN KEY (`reference_id`) REFERENCES `essay` (`essay_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of essay_reference
-- ----------------------------

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `essay_id` bigint(20) NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`essay_id`) USING BTREE,
  CONSTRAINT `note_ibfk_1` FOREIGN KEY (`essay_id`) REFERENCES `essay` (`essay_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of note
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(44) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `salt` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('N','S') CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL DEFAULT 'N',
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (3, 'rose', 'V1GeCLZ0CKIqC7J2VMzns5RBAw21baWZ97KmghbfzSk=', '8PqsiXcOPJa8H', '小红', 'happy@123.com', 'N', b'0');

SET FOREIGN_KEY_CHECKS = 1;
