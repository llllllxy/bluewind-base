/*
 Navicat MySQL Data Transfer

 Source Server         : 本地MySQL-127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50725
 Source Host           : localhost:3306
 Source Schema         : bluewind-base

 Target Server Type    : MySQL
 Target Server Version : 50725
 File Encoding         : 65001

 Date: 01/12/2022 09:53:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
                                   `permission_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
                                   `parent_id` bigint(20) NOT NULL COMMENT '父级菜单ID，一级菜单为0',
                                   `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
                                   `path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
                                   `permission_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限资源值标识',
                                   `type` int(11) NOT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
                                   `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
                                   `order_num` int(11) NOT NULL COMMENT '排序',
                                   `status` tinyint(4) NOT NULL COMMENT '状态 0:正常，1:停用',
                                   `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                                   `created_by` bigint(20) NOT NULL COMMENT '创建用户id',
                                   `updated_by` bigint(20) NULL DEFAULT NULL COMMENT '修改用户id',
                                   PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 0, '系统管理', '/system', '', 0, NULL, 1, 0, '2022-08-30 10:24:39', '2022-08-30 10:24:39', 1, NULL);
INSERT INTO `sys_permission` VALUES (2, 1, '用户管理', '/system/user/init', 'system:user:init', 1, NULL, 1, 0, '2022-08-30 10:25:17', '2022-08-30 10:26:17', 1, NULL);
INSERT INTO `sys_permission` VALUES (3, 2, '新增', '/system/user/add', 'system:user:add', 2, NULL, 1, 0, '2022-08-30 10:26:01', '2022-08-30 10:26:01', 1, NULL);
INSERT INTO `sys_permission` VALUES (4, 2, '修改', '/system/user/edit', 'system:user:edit', 2, NULL, 1, 0, '2022-08-30 10:28:31', '2022-08-30 10:28:41', 1, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
                             `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
                             `role_sign` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色标识',
                             `status` tinyint(4) NOT NULL COMMENT '状态 0:正常，1:停用',
                             `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                             `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                             `created_by` bigint(20) NOT NULL COMMENT '创建用户id',
                             `updated_by` bigint(20) NULL DEFAULT NULL COMMENT '修改用户id',
                             PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员角色', 'superadmin', 0, '超级管理员角色', '2022-08-30 10:21:09', '2022-08-30 10:21:09', 1, NULL);
INSERT INTO `sys_role` VALUES (2, '测试角色', 'test', 0, '测试角色', '2022-08-30 10:21:39', '2022-08-30 10:21:39', 1, NULL);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录id',
                                        `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                                        `permission_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
                                        `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与菜单对应关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1, '2022-08-30 10:29:31', '2022-08-30 10:29:31');
INSERT INTO `sys_role_permission` VALUES (2, 1, 2, '2022-08-30 10:29:35', '2022-08-30 10:29:35');
INSERT INTO `sys_role_permission` VALUES (3, 1, 3, '2022-08-30 10:29:38', '2022-08-30 10:29:38');
INSERT INTO `sys_role_permission` VALUES (4, 1, 4, '2022-08-30 10:29:42', '2022-08-30 10:29:42');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
                             `dept_id` bigint(20) NOT NULL COMMENT '所属部门机构',
                             `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
                             `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
                             `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名(昵称)',
                             `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                             `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
                             `sex` tinyint(4) NOT NULL COMMENT '性别 0:男，1:女，2:未知',
                             `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态 0:正常，1:停用',
                             `avatar_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '头像上传 0:未上传 1:上传',
                             `avatar` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像url(或者文档id)',
                             `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
                             `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                             `created_by` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
                             `updated_by` bigint(20) NULL DEFAULT NULL COMMENT '修改用户id',
                             PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 898934823, 'admin', '6172b7c9803ac7dc3c31b0bcdf4241f5e3994ffc802fed48bbdebe0f7e70eff9', '管理员(123456)', NULL, NULL, 1, 0, 0, NULL, NULL, '2022-08-26 16:50:12', '2022-08-30 10:20:10', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录id',
                                  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
                                  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
                                  `created_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updated_at` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1, '2022-08-30 10:29:12', '2022-08-30 10:29:12');
INSERT INTO `sys_user_role` VALUES (2, 1, 2, '2022-08-30 10:29:18', '2022-08-30 10:29:18');

SET FOREIGN_KEY_CHECKS = 1;
