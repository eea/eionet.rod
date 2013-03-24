SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `ACLS`;
CREATE TABLE `ACLS` (
  `ACL_ID` int(11) NOT NULL auto_increment,
  `ACL_NAME` varchar(100) NOT NULL default '',
  `PARENT_NAME` varchar(100) default NULL,
  `OWNER` varchar(255) NOT NULL default '',
  `DESCRIPTION` varchar(255) default '',
  PRIMARY KEY  (`ACL_ID`),
  UNIQUE KEY `ACL_NAME` (`ACL_NAME`,`PARENT_NAME`)
) ENGINE=MyISAM AUTO_INCREMENT=738 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `ACL_ROWS`;
CREATE TABLE `ACL_ROWS` (
  `ACL_ID` int(11) NOT NULL default '0',
  `TYPE` enum('object','doc','dcc') NOT NULL default 'object',
  `ENTRY_TYPE` enum('owner','user','localgroup','other','foreign','unauthenticated','authenticated','mask') NOT NULL default 'user',
  `PRINCIPAL` char(16) NOT NULL default '',
  `PERMISSIONS` char(255) NOT NULL default '',
  `STATUS` int(1) NOT NULL default '0',
  PRIMARY KEY  (`ACL_ID`,`TYPE`,`ENTRY_TYPE`,`PRINCIPAL`,`STATUS`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `HLP_AREA`;
CREATE TABLE `HLP_AREA` (
  `AREA_ID` varchar(100) NOT NULL default '',
  `SCREEN_ID` varchar(100) NOT NULL default '',
  `DESCRIPTION` text NOT NULL,
  `LANGUAGE` varchar(100) NOT NULL default '',
  `HTML` text NOT NULL,
  `MD5` varchar(32) NOT NULL default '',
  `POPUP_WIDTH` varchar(10) NOT NULL default '400',
  `POPUP_LENGTH` varchar(10) NOT NULL default '400',
  PRIMARY KEY  (`AREA_ID`,`SCREEN_ID`,`LANGUAGE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `HLP_SCREEN`;
CREATE TABLE `HLP_SCREEN` (
  `SCREEN_ID` varchar(100) NOT NULL default '',
  `DESCRIPTION` text NOT NULL,
  PRIMARY KEY  (`SCREEN_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_ATTRIBUTE`;
CREATE TABLE `T_ATTRIBUTE` (
  `ID` int(11) NOT NULL default '-1',
  `SCOPE` enum('obligations','instruments','clients','spatial') NOT NULL default 'obligations',
  `NAME` varchar(50) NOT NULL default '',
  `OBJECT` varchar(255) NOT NULL default '',
  `OBJECTLANG` varchar(10) NOT NULL default '',
  `TYPE` enum('reference','','string','integer','decimal','boolean','dateTime','date') default '',
  `LINKTEXT` varchar(64) default '',
  PRIMARY KEY  (`ID`,`SCOPE`,`NAME`,`OBJECT`,`OBJECTLANG`),
  KEY `PREDICATE` (`NAME`),
  KEY `OBJECT` (`OBJECT`),
  CONSTRAINT `T_ATTRIBUTE_ibfk_1` FOREIGN KEY (`NAME`) REFERENCES `T_NAMELABEL` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Object attributes';


DROP TABLE IF EXISTS `T_CLIENT`;
CREATE TABLE `T_CLIENT` (
  `PK_CLIENT_ID` int(10) unsigned NOT NULL auto_increment,
  `CLIENT_NAME` varchar(255) default NULL,
  `CLIENT_ACRONYM` varchar(100) default NULL,
  `CLIENT_URL` varchar(255) default NULL,
  `CLIENT_ADDRESS` varchar(255) default NULL,
  `CLIENT_EMAIL` varchar(255) default NULL,
  `DESCRIPTION` text,
  `POSTAL_CODE` varchar(100) default NULL,
  `CITY` varchar(255) default NULL,
  `COUNTRY` varchar(255) default NULL,
  `CLIENT_SHORT_NAME` varchar(100) default NULL,
  PRIMARY KEY  (`PK_CLIENT_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=100 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_CLIENT_LNK`;
CREATE TABLE `T_CLIENT_LNK` (
  `FK_CLIENT_ID` int(10) NOT NULL default '0',
  `FK_OBJECT_ID` int(10) NOT NULL default '0',
  `TYPE` char(1) NOT NULL default '',
  `STATUS` enum('M','C') NOT NULL default 'M',
  PRIMARY KEY  (`FK_CLIENT_ID`,`FK_OBJECT_ID`,`TYPE`,`STATUS`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_DELIVERY`;
CREATE TABLE `T_DELIVERY` (
  `RA_URL` text NOT NULL,
  `TITLE` text NOT NULL,
  `UPLOAD_DATE` date default NULL,
  `DELIVERY_URL` text NOT NULL,
  `TYPE` varchar(100) default NULL,
  `FORMAT` varchar(100) default NULL,
  `FK_RA_ID` int(10) default NULL,
  `FK_SPATIAL_ID` int(11) default NULL,
  `COVERAGE` varchar(255) default NULL,
  `COVERAGE_NOTE` text,
  `STATUS` int(1) default NULL,
  KEY `fk_ra_id` (`FK_RA_ID`),
  KEY `fk_spatial_id` (`FK_SPATIAL_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Delivery information from Content Registry to the reporting ';


DROP TABLE IF EXISTS `T_DUMMY`;
CREATE TABLE `T_DUMMY` (
  `DUMMY` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_HELP`;
CREATE TABLE `T_HELP` (
  `PK_HELP_ID` varchar(50) NOT NULL default '',
  `HELP_TITLE` varchar(255) default NULL,
  `HELP_TEXT` text,
  PRIMARY KEY  (`PK_HELP_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_HISTORIC_DEADLINES`;
CREATE TABLE `T_HISTORIC_DEADLINES` (
  `FK_RA_ID` int(10) NOT NULL default '0',
  `DEADLINE` date NOT NULL default '0000-00-00',
  PRIMARY KEY  (`FK_RA_ID`,`DEADLINE`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_HISTORY`;
CREATE TABLE `T_HISTORY` (
  `PK_HISTORY_ID` int(10) unsigned NOT NULL auto_increment,
  `ITEM_ID` int(10) default NULL,
  `ITEM_TYPE` char(1) default NULL,
  `ACTION_TYPE` char(1) default NULL,
  `LOG_TIME` datetime default NULL,
  `USER` varchar(100) default NULL,
  `DESCRIPTION` text,
  PRIMARY KEY  (`PK_HISTORY_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=24946 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_INDICATOR`;
CREATE TABLE `T_INDICATOR` (
  `PK_INDICATOR_ID` int(10) NOT NULL auto_increment,
  `FK_RA_ID` int(10) NOT NULL default '0',
  `NUMBER` varchar(100) NOT NULL default '',
  `TITLE` varchar(255) default NULL,
  `URL` varchar(255) default NULL,
  `OWNER` varchar(255) default NULL,
  PRIMARY KEY  (`PK_INDICATOR_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_INFO_LNK`;
CREATE TABLE `T_INFO_LNK` (
  `FK_RA_ID` int(10) NOT NULL default '0',
  `FK_INFO_ID` char(1) NOT NULL default '',
  PRIMARY KEY  (`FK_RA_ID`,`FK_INFO_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_ISSUE`;
CREATE TABLE `T_ISSUE` (
  `PK_ISSUE_ID` int(11) unsigned NOT NULL auto_increment,
  `ISSUE_NAME` varchar(100) NOT NULL default '',
  `FK_DD_GUID` int(11) unsigned default NULL,
  `DD_URL` varchar(255) default NULL,
  PRIMARY KEY  (`PK_ISSUE_ID`),
  KEY `PK_ISSUE_ID` (`PK_ISSUE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_LOOKUP`;
CREATE TABLE `T_LOOKUP` (
  `C_TERM` varchar(100) NOT NULL default '',
  `CATEGORY` enum('1','2','C','P','L','S','F','UT','ST','I','DGS') NOT NULL default 'P',
  `C_VALUE` char(1) default NULL,
  PRIMARY KEY  (`C_TERM`,`CATEGORY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_NAMELABEL`;
CREATE TABLE `T_NAMELABEL` (
  `NAME` varchar(50) NOT NULL default '',
  `LABEL` varchar(64) default '',
  PRIMARY KEY  (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Labels for names in T_ATTRIBUTE';


DROP TABLE IF EXISTS `T_OBLIGATION`;
CREATE TABLE `T_OBLIGATION` (
  `PK_RA_ID` int(10) NOT NULL auto_increment,
  `FK_SOURCE_ID` int(10) unsigned NOT NULL default '0',
  `VALID_SINCE` date default NULL,
  `TITLE` varchar(255) NOT NULL default '',
  `FORMAT_NAME` varchar(100) default NULL,
  `REPORT_FORMAT_URL` varchar(255) default NULL,
  `REPORT_FREQ_DETAIL` varchar(50) default NULL,
  `REPORTING_FORMAT` text,
  `DATE_COMMENTS` varchar(255) default NULL,
  `LAST_UPDATE` date default NULL,
  `TERMINATE` enum('Y','N') NOT NULL default 'N',
  `REPORT_FREQ` varchar(25) default NULL,
  `COMMENT` text,
  `RESPONSIBLE_ROLE` varchar(255) default NULL,
  `NEXT_DEADLINE` date default NULL,
  `FIRST_REPORTING` date default NULL,
  `REPORT_FREQ_MONTHS` int(3) default NULL,
  `NEXT_REPORTING` varchar(255) default NULL,
  `VALID_TO` date default NULL,
  `FK_DELIVERY_COUNTRY_IDS` varchar(255) default NULL,
  `NEXT_DEADLINE2` date default NULL,
  `RM_NEXT_UPDATE` date default NULL,
  `RM_VERIFIED` date default NULL,
  `RM_VERIFIED_BY` varchar(50) default NULL,
  `LAST_HARVESTED` datetime default NULL,
  `LOCATION_PTR` varchar(255) default NULL,
  `LOCATION_INFO` varchar(255) default NULL,
  `LEGAL_MORAL` enum('L','M','V') default 'L',
  `FK_CLIENT_ID` int(10) default NULL,
  `DESCRIPTION` text,
  `RESPONSIBLE_ROLE_SUF` int(1) NOT NULL default '1',
  `NATIONAL_CONTACT` varchar(255) default NULL,
  `NATIONAL_CONTACT_URL` varchar(255) default NULL,
  `COORDINATOR_ROLE` varchar(255) default NULL,
  `COORDINATOR_ROLE_SUF` int(1) NOT NULL default '1',
  `COORDINATOR` varchar(255) default NULL,
  `COORDINATOR_URL` varchar(255) default NULL,
  `PARAMETERS` text,
  `VALIDATED_BY` text,
  `EEA_PRIMARY` int(1) default '0',
  `EEA_CORE` int(1) default '0',
  `FLAGGED` int(1) default '0',
  `AUTHORITY` varchar(255) default NULL,
  `OVERLAP_URL` varchar(255) default NULL,
  `DPSIR_D` enum('yes','no') default NULL,
  `DPSIR_P` enum('yes','no') default NULL,
  `DPSIR_S` enum('yes','no') default NULL,
  `DPSIR_I` enum('yes','no') default NULL,
  `DPSIR_R` enum('yes','no') default NULL,
  `DATA_USED_FOR` varchar(255) default NULL,
  `DATA_USED_FOR_URL` varchar(255) default NULL,
  `CONTINOUS_REPORTING` enum('yes','no') default NULL,
  PRIMARY KEY  (`PK_RA_ID`),
  KEY `FK_SOURCE_ID` (`FK_SOURCE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=692 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_RAISSUE_LNK`;
CREATE TABLE `T_RAISSUE_LNK` (
  `FK_ISSUE_ID` int(10) unsigned NOT NULL default '0',
  `FK_RA_ID` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`FK_ISSUE_ID`,`FK_RA_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_RASPATIAL_LNK`;
CREATE TABLE `T_RASPATIAL_LNK` (
  `FK_SPATIAL_ID` int(10) unsigned NOT NULL default '0',
  `FK_RA_ID` int(10) unsigned NOT NULL default '0',
  `VOLUNTARY` enum('Y','N') NOT NULL default 'Y',
  PRIMARY KEY  (`FK_SPATIAL_ID`,`FK_RA_ID`,`VOLUNTARY`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_ROLE`;
CREATE TABLE `T_ROLE` (
  `ROLE_NAME` varchar(255) NOT NULL default '',
  `ROLE_EMAIL` varchar(255) NOT NULL default '',
  `ROLE_URL` text,
  `ROLE_ID` varchar(255) NOT NULL default '',
  `ROLE_MEMBERS_URL` varchar(255) default NULL,
  `LAST_HARVESTED` datetime default NULL,
  `STATUS` int(1) NOT NULL default '0',
  PRIMARY KEY  (`ROLE_ID`,`STATUS`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Role information from DIRECTORY';


DROP TABLE IF EXISTS `T_ROLE_OCCUPANTS`;
CREATE TABLE `T_ROLE_OCCUPANTS` (
  `ROLE_ID` varchar(255) NOT NULL default '',
  `PERSON` varchar(255) default NULL,
  `INSTITUTE` varchar(255) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_SOURCE`;
CREATE TABLE `T_SOURCE` (
  `PK_SOURCE_ID` int(10) unsigned NOT NULL auto_increment,
  `FK_TYPE_ID` int(10) unsigned NOT NULL default '0',
  `SOURCE_CODE` varchar(50) NOT NULL default '',
  `TITLE` text NOT NULL,
  `CELEX_REF` varchar(255) default NULL,
  `LEGAL_NAME` varchar(255) NOT NULL default '',
  `URL` text,
  `SECRETARIAT` text,
  `SECRETARIAT_URL` varchar(255) default NULL,
  `ABSTRACT` text,
  `EC_ACCESSION` date default NULL,
  `EC_ENTRY_INTO_FORCE` date default NULL,
  `DRAFT` enum('Y','N') default NULL,
  `COMMENT` text,
  `LAST_MODIFIED` date default NULL,
  `ISSUED_BY` text,
  `VALID_FROM` date default NULL,
  `ALIAS` text,
  `ISSUED_BY_URL` varchar(255) default NULL,
  `FK_CLIENT_ID` int(10) default NULL,
  `LAST_UPDATE` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `RM_NEXT_UPDATE` date default NULL,
  `RM_VERIFIED` date default NULL,
  `RM_VERIFIED_BY` varchar(50) default NULL,
  `GEOGRAPHIC_SCOPE` varchar(255) default NULL,
  `DGENV_REVIEW` char(1) default NULL,
  `RM_VALIDATED_BY` varchar(50) default NULL,
  PRIMARY KEY  (`PK_SOURCE_ID`),
  KEY `FK_TYPE_ID` (`FK_TYPE_ID`),
  KEY `LEGAL_NAME` (`LEGAL_NAME`),
  KEY `FT_TITLE` (`TITLE`(1)),
  KEY `TITLE` (`TITLE`(20)),
  KEY `FT_ALIAS` (`ALIAS`(1)),
  KEY `ALIAS` (`ALIAS`(20))
) ENGINE=MyISAM AUTO_INCREMENT=652 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_SOURCE_CLASS`;
CREATE TABLE `T_SOURCE_CLASS` (
  `PK_CLASS_ID` int(10) unsigned NOT NULL auto_increment,
  `CLASSIFICATOR` varchar(20) default NULL,
  `CLASS_NAME` varchar(255) NOT NULL default '',
  `NEW` tinyint(3) unsigned NOT NULL default '1',
  PRIMARY KEY  (`PK_CLASS_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 PACK_KEYS=1;


DROP TABLE IF EXISTS `T_SOURCE_LNK`;
CREATE TABLE `T_SOURCE_LNK` (
  `PK_SOURCE_LNK_ID` int(10) unsigned NOT NULL auto_increment,
  `FK_SOURCE_CHILD_ID` int(10) unsigned NOT NULL default '0',
  `CHILD_TYPE` enum('S','C') NOT NULL default 'S',
  `FK_SOURCE_PARENT_ID` int(10) unsigned NOT NULL default '0',
  `PARENT_TYPE` enum('S','C') NOT NULL default 'S',
  PRIMARY KEY  (`PK_SOURCE_LNK_ID`),
  UNIQUE KEY `FK_SOURCE_CHILD_ID_2` (`FK_SOURCE_CHILD_ID`,`CHILD_TYPE`,`FK_SOURCE_PARENT_ID`,`PARENT_TYPE`),
  KEY `FK_SOURCE_CHILD_ID` (`FK_SOURCE_CHILD_ID`,`CHILD_TYPE`),
  KEY `FK_SOURCE_PARENT_ID` (`FK_SOURCE_PARENT_ID`,`PARENT_TYPE`)
) ENGINE=MyISAM AUTO_INCREMENT=3048 DEFAULT CHARSET=utf8 PACK_KEYS=1;


DROP TABLE IF EXISTS `T_SOURCE_TYPE`;
CREATE TABLE `T_SOURCE_TYPE` (
  `PK_TYPE_ID` int(10) unsigned NOT NULL auto_increment,
  `TYPE` varchar(255) NOT NULL default '',
  `NEW` tinyint(3) unsigned NOT NULL default '1',
  PRIMARY KEY  (`PK_TYPE_ID`),
  KEY `TYPE` (`TYPE`)
) ENGINE=MyISAM AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 PACK_KEYS=1;


DROP TABLE IF EXISTS `T_SPATIAL`;
CREATE TABLE `T_SPATIAL` (
  `PK_SPATIAL_ID` int(11) NOT NULL auto_increment,
  `SPATIAL_NAME` varchar(100) NOT NULL default '',
  `SPATIAL_TYPE` char(1) NOT NULL default '',
  `FK_DD_GUID` int(11) default NULL,
  `DD_URL` varchar(255) default NULL,
  `SPATIAL_TWOLETTER` varchar(2) default NULL,
  `SPATIAL_ISMEMBERCOUNTRY` enum('Y','N') default NULL,
  PRIMARY KEY  (`PK_SPATIAL_ID`),
  KEY `PK_SPATIAL_ID` (`PK_SPATIAL_ID`),
  KEY `SPATIAL_TYPE` (`SPATIAL_TYPE`)
) ENGINE=MyISAM AUTO_INCREMENT=122 DEFAULT CHARSET=utf8 COMMENT='SpatialCoverage';


DROP TABLE IF EXISTS `T_SPATIAL_HISTORY`;
CREATE TABLE `T_SPATIAL_HISTORY` (
  `FK_SPATIAL_ID` int(10) unsigned NOT NULL default '0',
  `FK_RA_ID` int(10) unsigned NOT NULL default '0',
  `VOLUNTARY` enum('Y','N') default 'N',
  `START_DATE` date default NULL,
  `END_DATE` date default NULL,
  `PK_SPATIAL_HISTORY_ID` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`PK_SPATIAL_HISTORY_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=16427 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `T_UNDO`;
CREATE TABLE `T_UNDO` (
  `undo_time` bigint(20) NOT NULL default '0',
  `tab` varchar(32) NOT NULL default '',
  `col` varchar(32) NOT NULL default '',
  `operation` enum('I','D','U','A','L','K','O','UN','UD','UDD','T','ACL') NOT NULL default 'I',
  `quotes` enum('n','y') default NULL,
  `p_key` enum('n','y') default NULL,
  `value` blob,
  `sub_trans_nr` int(11) NOT NULL default '0',
  `show_object` enum('n','y') default NULL,
  PRIMARY KEY  (`undo_time`,`tab`,`col`,`operation`,`sub_trans_nr`),
  KEY `col` (`col`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `X_ACTIVITY`;
CREATE TABLE `X_ACTIVITY` (
  `PK_RA_ID` int(10) unsigned NOT NULL auto_increment,
  `FK_RO_ID` int(10) unsigned NOT NULL default '0',
  `FK_SOURCE_ID` int(10) unsigned NOT NULL default '0',
  `VALID_SINCE` date default NULL,
  `TITLE` varchar(255) NOT NULL default '',
  `FORMAT_NAME` varchar(100) default NULL,
  `REPORT_FORMAT_URL` varchar(255) default NULL,
  `REPORT_FREQ_DETAIL` varchar(50) default NULL,
  `REPORTING_FORMAT` text,
  `DATE_COMMENTS` varchar(255) default NULL,
  `LAST_UPDATE` date default NULL,
  `TERMINATE` enum('Y','N') NOT NULL default 'N',
  `REPORT_FREQ` varchar(25) default NULL,
  `COMMENT` text,
  `RESPONSIBLE_ROLE` varchar(255) default NULL,
  `NEXT_DEADLINE` date default NULL,
  `FIRST_REPORTING` date default NULL,
  `REPORT_FREQ_MONTHS` int(3) default NULL,
  `NEXT_REPORTING` varchar(255) default NULL,
  `VALID_TO` date default NULL,
  `FK_DELIVERY_COUNTRY_IDS` varchar(255) default NULL,
  `NEXT_DEADLINE2` date default NULL,
  `RM_NEXT_UPDATE` date default NULL,
  `RM_VERIFIED` date default NULL,
  `RM_VERIFIED_BY` varchar(50) default NULL,
  `LAST_HARVESTED` datetime default NULL,
  `LOCATION_PTR` varchar(255) default NULL,
  `LOCATION_INFO` varchar(255) default NULL,
  PRIMARY KEY  (`PK_RA_ID`),
  KEY `FK_REPORTING_ID` (`FK_RO_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=386 DEFAULT CHARSET=utf8 COMMENT='Reporting Activity';


DROP TABLE IF EXISTS `X_CLIENT_LNK`;
CREATE TABLE `X_CLIENT_LNK` (
  `FK_CLIENT_ID` int(10) default NULL,
  `FK_OBJECT_ID` int(10) default NULL,
  `TYPE` char(1) default NULL,
  `STATUS` char(1) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `X_PARAMETER`;
CREATE TABLE `X_PARAMETER` (
  `PK_PARAMETER_ID` int(10) unsigned NOT NULL auto_increment,
  `PARAMETER_NAME` varchar(100) NOT NULL default '',
  `FK_GROUP_ID` int(11) NOT NULL default '0',
  `FK_DD_GUID` int(11) unsigned default NULL,
  `DD_URL` varchar(255) default NULL,
  `NEW` tinyint(3) unsigned NOT NULL default '1',
  PRIMARY KEY  (`PK_PARAMETER_ID`),
  KEY `FK_GROUP_ID` (`FK_GROUP_ID`),
  KEY `PARAMETER_NAME` (`PARAMETER_NAME`)
) ENGINE=MyISAM AUTO_INCREMENT=489 DEFAULT CHARSET=utf8 PACK_KEYS=1;


DROP TABLE IF EXISTS `X_PARAMETER_LNK`;
CREATE TABLE `X_PARAMETER_LNK` (
  `PK_PARAMETER_LNK_ID` int(10) unsigned NOT NULL auto_increment,
  `FK_PARAMETER_ID` int(10) unsigned NOT NULL default '0',
  `PARAMETER_UNIT` varchar(50) default NULL,
  `FK_RA_ID` int(10) unsigned NOT NULL default '0',
  `FK_UNIT_ID` int(11) default NULL,
  PRIMARY KEY  (`PK_PARAMETER_LNK_ID`),
  KEY `FK_PARAMETER_ID` (`FK_PARAMETER_ID`),
  KEY `FK_RA_ID` (`FK_RA_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=1261 DEFAULT CHARSET=utf8 PACK_KEYS=1;


DROP TABLE IF EXISTS `X_PARAM_GROUP`;
CREATE TABLE `X_PARAM_GROUP` (
  `PK_GROUP_ID` int(11) NOT NULL auto_increment,
  `GROUP_NAME` varchar(100) NOT NULL default '',
  `FK_DD_GUID` int(11) default NULL,
  `DD_URL` varchar(255) default NULL,
  `GROUP_TYPE` enum('C','S') NOT NULL default 'C',
  PRIMARY KEY  (`PK_GROUP_ID`),
  KEY `PK_GROUP_ID` (`PK_GROUP_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT='GROUP';


DROP TABLE IF EXISTS `X_REPORTING`;
CREATE TABLE `X_REPORTING` (
  `PK_RO_ID` int(10) unsigned NOT NULL auto_increment,
  `FK_SOURCE_ID` int(10) unsigned NOT NULL default '0',
  `LEGAL_MORAL` enum('L','M','V') NOT NULL default 'L',
  `LAST_UPDATE` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `ALIAS` varchar(255) default NULL,
  `REPORT_TO` text,
  `VALID_FROM` date default NULL,
  `RECOGNIZED` enum('Y','N') NOT NULL default 'N',
  `RECOGNIZED_DETAIL` varchar(255) default NULL,
  `REPORT_TO_ROLE` varchar(255) default NULL,
  `FK_CLIENT_ID` int(10) default NULL,
  `RM_NEXT_UPDATE` date default NULL,
  `RM_VERIFIED` date default NULL,
  `RM_VERIFIED_BY` varchar(50) default NULL,
  PRIMARY KEY  (`PK_RO_ID`),
  KEY `LEGAL_MORAL` (`LEGAL_MORAL`),
  KEY `FK_SOURCE_ID` (`FK_SOURCE_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=362 DEFAULT CHARSET=utf8 PACK_KEYS=1;


DROP TABLE IF EXISTS `X_SPATIAL_LNK`;
CREATE TABLE `X_SPATIAL_LNK` (
  `PK_SPATIAL_LNK_ID` int(11) NOT NULL auto_increment,
  `FK_SPATIAL_ID` int(11) NOT NULL default '0',
  `FK_RO_ID` int(11) NOT NULL default '0',
  `VOLUNTARY` enum('Y','N') default 'N',
  PRIMARY KEY  (`PK_SPATIAL_LNK_ID`),
  KEY `FK_SPATIAL_ID` (`FK_SPATIAL_ID`),
  KEY `FK_RO_ID` (`FK_RO_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=10193 DEFAULT CHARSET=utf8 COMMENT='Links spatial coverage and reporting obligation elements';


DROP TABLE IF EXISTS `X_UNIT`;
CREATE TABLE `X_UNIT` (
  `PK_UNIT_ID` int(11) unsigned NOT NULL auto_increment,
  `UNIT_NAME` varchar(50) NOT NULL default '',
  `UNIT_HELP` text,
  PRIMARY KEY  (`PK_UNIT_ID`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='Parameter unit types';


SET FOREIGN_KEY_CHECKS=1;
