# Host: localhost  (Version: 5.6.23-log)
# Date: 2015-08-06 17:28:26
# Generator: MySQL-Front 5.3  (Build 4.89)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "pt_button"
#

DROP TABLE IF EXISTS `pt_button`;
CREATE TABLE `pt_button` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(4) DEFAULT NULL,
  `functionid` bigint(20) DEFAULT NULL COMMENT '关联节点',
  `functionname` varchar(50) DEFAULT NULL COMMENT '功能名称',
  `name` varchar(20) DEFAULT NULL COMMENT '按钮名称',
  `value` varchar(50) DEFAULT NULL COMMENT '按钮代号',
  `url` varchar(200) DEFAULT NULL COMMENT '触发URL',
  `orderid` varchar(20) DEFAULT NULL COMMENT '排序',
  `created_at` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `created_userid` bigint(20) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `updated_userid` bigint(20) DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '禁用时间',
  `deleted_userid` bigint(20) DEFAULT NULL COMMENT '禁用人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='权限按钮';

#
# Data for table "pt_button"
#

INSERT INTO `pt_button` VALUES (1,0,3,'角色管理','查询','query','ftww/basic/role/query',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,0,3,'角色管理','修改','update','ftww/basic/role/update',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

#
# Structure for table "pt_company"
#

DROP TABLE IF EXISTS `pt_company`;
CREATE TABLE `pt_company` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `version` int(4) DEFAULT NULL,
  `userid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `employeeid` varchar(50) DEFAULT NULL COMMENT '员工编号',
  `positionid` bigint(20) DEFAULT NULL COMMENT '职务',
  `join_at` timestamp NULL DEFAULT NULL COMMENT '入职时间',
  `leave_at` timestamp NULL DEFAULT NULL COMMENT '离职时间',
  `status` varchar(5) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司';

#
# Data for table "pt_company"
#

INSERT INTO `pt_company` VALUES (1,0,1,'322221',1,NULL,NULL,NULL);

#
# Structure for table "pt_department"
#

DROP TABLE IF EXISTS `pt_department`;
CREATE TABLE `pt_department` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `version` int(4) DEFAULT NULL,
  `name` varchar(25) DEFAULT NULL COMMENT '机构名称',
  `abbreviate` varchar(50) DEFAULT NULL COMMENT '机构简称',
  `description` varchar(200) DEFAULT NULL COMMENT '机构描述',
  `depttype` char(1) DEFAULT NULL COMMENT '机构类型',
  `telephone` varchar(30) DEFAULT NULL COMMENT '办公室电话',
  `fax` varchar(50) DEFAULT NULL COMMENT '传真',
  `postcode` varchar(6) DEFAULT NULL COMMENT '邮编',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `suborgs` int(4) DEFAULT NULL COMMENT '子机构数量',
  `orgemplyees` varchar(255) DEFAULT '0' COMMENT '本机构员工数',
  `orgallemployees` int(4) DEFAULT NULL COMMENT '本机构全部员工数',
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点ID',
  `pidpath` varchar(200) DEFAULT '' COMMENT '父节点ID路径',
  `deptlevel` int(4) DEFAULT NULL COMMENT '机构层数',
  `allchildnodeids` varchar(2000) DEFAULT NULL COMMENT '所有子节点',
  `orderid` int(4) DEFAULT NULL COMMENT '排序',
  `images` varchar(50) DEFAULT NULL COMMENT '图标',
  `isparent` varchar(5) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `principaluserids` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='部门';

#
# Data for table "pt_department"
#

INSERT INTO `pt_department` VALUES (1,0,'研发部',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,0,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,0,'产品部',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL,0,'',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

#
# Structure for table "pt_department_user"
#

DROP TABLE IF EXISTS `pt_department_user`;
CREATE TABLE `pt_department_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `departmentid` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `userid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='部门-人员';

#
# Data for table "pt_department_user"
#

INSERT INTO `pt_department_user` VALUES (1,1,1),(2,2,1),(3,1,3);

#
# Structure for table "pt_function"
#

DROP TABLE IF EXISTS `pt_function`;
CREATE TABLE `pt_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '功能名称',
  `value` varchar(50) DEFAULT NULL COMMENT '功能代号',
  `url` varchar(200) DEFAULT NULL COMMENT '请求资源URL',
  `description` varchar(200) DEFAULT NULL COMMENT '功能描述',
  `pid` bigint(20) DEFAULT NULL,
  `pidpath` varchar(200) DEFAULT NULL,
  `orderid` int(4) DEFAULT NULL COMMENT '排序',
  `splitpage` char(1) DEFAULT NULL COMMENT '是否分页',
  `formtoken` char(1) DEFAULT NULL COMMENT '表单提交验证',
  `created_at` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `created_userid` bigint(20) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `updated_userid` bigint(20) DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '禁用时间',
  `deleted_userid` bigint(20) DEFAULT NULL COMMENT '禁用人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='权限功能';

#
# Data for table "pt_function"
#

INSERT INTO `pt_function` VALUES (1,0,'系统管理','F_SYSTEM','/ftww/basic/system/**',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,0,'用户管理','F_USER','/ftww/basic/user/**',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,0,'角色管理','F_ROLE','/ftww/basic/role/**',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,0,'新增角色','F_ROLE_ADD','/ftww/basic/role/add/**',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

#
# Structure for table "pt_position"
#

DROP TABLE IF EXISTS `pt_position`;
CREATE TABLE `pt_position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(4) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '岗位名称',
  `pid` bigint(20) DEFAULT NULL,
  `pidpath` varchar(200) DEFAULT NULL,
  `orderid` int(4) DEFAULT NULL COMMENT '排序',
  `created_at` timestamp NULL DEFAULT NULL,
  `created_userid` bigint(20) DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `updated_userid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='岗位';

#
# Data for table "pt_position"
#

INSERT INTO `pt_position` VALUES (1,0,'工程师',NULL,NULL,NULL,NULL,NULL,NULL,NULL);

#
# Structure for table "pt_role"
#

DROP TABLE IF EXISTS `pt_role`;
CREATE TABLE `pt_role` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `version` int(4) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `value` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `pid` bigint(20) DEFAULT NULL COMMENT '父节点ID',
  `pidpath` varchar(200) DEFAULT '' COMMENT '父节点ID的路径。为n级分类做预留',
  `rolelevel` int(4) DEFAULT NULL COMMENT '层级',
  `orderid` int(4) DEFAULT NULL COMMENT '排序',
  `functionids` text COMMENT '角色拥有的功能节点。',
  `created_at` timestamp NULL DEFAULT NULL COMMENT '添加日期',
  `created_userid` bigint(20) DEFAULT NULL COMMENT '添加人员',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `updated_userid` bigint(20) DEFAULT NULL COMMENT '修改人员',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '禁用时间',
  `deleted_userid` bigint(20) DEFAULT NULL COMMENT '禁用人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色表';

#
# Data for table "pt_role"
#

INSERT INTO `pt_role` VALUES (1,0,'管理员','R_ADMIN',NULL,NULL,'',NULL,NULL,'1,2,3',NULL,NULL,NULL,NULL,NULL,NULL);

#
# Structure for table "pt_role_button"
#

DROP TABLE IF EXISTS `pt_role_button`;
CREATE TABLE `pt_role_button` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `roleid` varchar(32) DEFAULT NULL COMMENT '角色ID',
  `functionid` varchar(32) DEFAULT NULL COMMENT '功能ID',
  `buttonids` text COMMENT '按钮IDs',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能节点按钮权限';

#
# Data for table "pt_role_button"
#

INSERT INTO `pt_role_button` VALUES ('1','1','3','1,2');

#
# Structure for table "pt_role_user"
#

DROP TABLE IF EXISTS `pt_role_user`;
CREATE TABLE `pt_role_user` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `roleid` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `userid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色里包含的用户';

#
# Data for table "pt_role_user"
#

INSERT INTO `pt_role_user` VALUES (1,1,1),(2,1,2);

#
# Structure for table "pt_syslog"
#

DROP TABLE IF EXISTS `pt_syslog`;
CREATE TABLE `pt_syslog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version` int(4) DEFAULT NULL,
  `actionenddate` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `actionendtime` bigint(20) DEFAULT NULL COMMENT '结束时间戳',
  `actionspend` varchar(255) DEFAULT NULL,
  `actionstartdate` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `actionstarttime` bigint(20) DEFAULT NULL COMMENT '结束时间戳',
  `cause` char(1) DEFAULT NULL,
  `cookie` varchar(1024) DEFAULT NULL COMMENT 'COOKIES',
  `description` text COMMENT '错误描述',
  `enddate` timestamp NULL DEFAULT NULL,
  `endtime` bigint(20) DEFAULT NULL,
  `spend` bigint(20) DEFAULT NULL,
  `ips` varchar(128) DEFAULT NULL,
  `method` varchar(8) DEFAULT NULL COMMENT '提交方式',
  `referer` varchar(500) DEFAULT NULL,
  `requestpath` text,
  `startdate` timestamp NULL DEFAULT NULL,
  `starttime` bigint(20) DEFAULT NULL,
  `status` char(1) DEFAULT NULL,
  `useragent` varchar(200) DEFAULT NULL,
  `viewspend` bigint(20) DEFAULT NULL COMMENT '视图耗时',
  `functionid` bigint(20) DEFAULT NULL COMMENT '操作ID',
  `accept` varchar(200) DEFAULT NULL,
  `acceptencoding` varchar(200) DEFAULT NULL,
  `acceptlanguage` varchar(200) DEFAULT NULL,
  `connection` varchar(200) DEFAULT NULL,
  `host` varchar(200) DEFAULT NULL,
  `xrequestedwith` varchar(200) DEFAULT NULL,
  `pvids` varchar(32) DEFAULT NULL,
  `userid` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统操作日志';

#
# Data for table "pt_syslog"
#


#
# Structure for table "pt_user"
#

DROP TABLE IF EXISTS `pt_user`;
CREATE TABLE `pt_user` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `userinfoid` bigint(20) DEFAULT NULL COMMENT '人员ID',
  `username` varchar(50) DEFAULT NULL COMMENT '登陆账号',
  `password` varchar(200) DEFAULT NULL COMMENT 'MD5加密',
  `hasher` varchar(200) DEFAULT NULL,
  `salt` varchar(200) DEFAULT NULL COMMENT '加密盐',
  `departmentids` text COMMENT '可以查看哪些部门的相关信息',
  `logincount` int(4) DEFAULT NULL COMMENT '登陆次数',
  `last_login_at` timestamp NULL DEFAULT NULL COMMENT '最后一次登陆时间',
  `last_visit_at` timestamp NULL DEFAULT NULL COMMENT '最后访问时间，用于在线统计',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人员登陆信息';

#
# Data for table "pt_user"
#

INSERT INTO `pt_user` VALUES (1,1,'admin','$shiro1$SHA-256$500000$mlN8mhW79ow4YFxKS+k1dQ==$bqZY1+Pb4ks6MwMSzMyB8+9eMfGjcnUQFKLso3IgZTE=','default_hasher',NULL,'1,2,3,4,5',NULL,NULL,NULL),(2,2,'poicom','$shiro1$SHA-256$500000$mlN8mhW79ow4YFxKS+k1dQ==$bqZY1+Pb4ks6MwMSzMyB8+9eMfGjcnUQFKLso3IgZTE=','default_hasher',NULL,'1,2',NULL,NULL,NULL),(3,3,'ftww','$shiro1$SHA-256$500000$mlN8mhW79ow4YFxKS+k1dQ==$bqZY1+Pb4ks6MwMSzMyB8+9eMfGjcnUQFKLso3IgZTE=','default_hasher',NULL,'1,2',NULL,NULL,NULL);

#
# Structure for table "pt_user_info"
#

DROP TABLE IF EXISTS `pt_user_info`;
CREATE TABLE `pt_user_info` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `version` int(4) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL COMMENT '姓名',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别',
  `birthday` timestamp NULL DEFAULT NULL COMMENT '出生日期',
  `idcard` varchar(22) DEFAULT NULL COMMENT '身份证号码',
  `country` varchar(20) DEFAULT NULL COMMENT '国家',
  `provinceid` bigint(20) DEFAULT NULL COMMENT '省份ID',
  `cityid` bigint(20) DEFAULT NULL COMMENT '城市ID',
  `postcode` varchar(6) DEFAULT NULL COMMENT '邮编',
  `address` varchar(50) DEFAULT NULL COMMENT '住址',
  `created_at` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `created_userid` bigint(20) DEFAULT NULL COMMENT '添加人',
  `updated_at` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  `updated_userid` bigint(20) DEFAULT NULL COMMENT '修改人员',
  `deleted_at` timestamp NULL DEFAULT NULL COMMENT '禁用时间',
  `deleted_userid` bigint(20) DEFAULT NULL COMMENT '禁用人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='人员基本信息';

#
# Data for table "pt_user_info"
#

INSERT INTO `pt_user_info` VALUES (1,0,'ftww',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,0,'tercel',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

#
# Trigger "delete_user"
#

DROP TRIGGER IF EXISTS `delete_user`;
CREATE DEFINER='root'@'localhost' TRIGGER `ftww`.`delete_user` BEFORE DELETE ON `ftww`.`pt_department_user`
  FOR EACH ROW begin
  update pt_department set orgemplyees = orgemplyees - 1 where id = OLD.departmentid;
end;

#
# Trigger "insert_user"
#

DROP TRIGGER IF EXISTS `insert_user`;
CREATE DEFINER='root'@'localhost' TRIGGER `ftww`.`insert_user` BEFORE INSERT ON `ftww`.`pt_department_user`
  FOR EACH ROW begin
  update pt_department set orgemplyees = orgemplyees + 1 where id = NEW.departmentid;
end;
