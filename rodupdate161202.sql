CREATE TABLE T_HISTORY (PK_HISTORY_ID INT (10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  ITEM_ID INT (10),
  ITEM_TYPE CHAR(1),
	ACTION_TYPE CHAR(1),
	LOG_TIME DATETIME,
	USER VARCHAR(100),
	DESCRIPTION TEXT );


INSERT INTO T_HISTORY (ITEM_ID, ITEM_TYPE, ACTION_TYPE, LOG_TIME, USER, DESCRIPTION )
	SELECT PK_RO_ID, 'O', 'I', CURDATE(), 'webrod', IF(ALIAS IS NULL, '', ALIAS) FROM T_REPORTING;

INSERT INTO T_HISTORY (ITEM_ID, ITEM_TYPE, ACTION_TYPE, LOG_TIME, USER, DESCRIPTION )
	SELECT PK_RA_ID, 'A', 'I', CURDATE(), 'webrod', IF(TITLE IS NULL, '', TITLE) FROM T_ACTIVITY;

INSERT INTO T_HISTORY (ITEM_ID, ITEM_TYPE, ACTION_TYPE, LOG_TIME, USER, DESCRIPTION )
	SELECT PK_SOURCE_ID, 'L', 'I', CURDATE(), 'webrod', IF(TITLE IS NULL, '', TITLE) FROM T_SOURCE;

CREATE TABLE T_RAISSUE_LNK (
  FK_ISSUE_ID INT(10) UNSIGNED NOT NULL DEFAULT 0,
  FK_RA_ID INT(10) UNSIGNED NOT NULL DEFAULT 0,
  PRIMARY KEY  (FK_ISSUE_ID,FK_RA_ID)
);

INSERT INTO T_RAISSUE_LNK
SELECT DISTINCT T_ACTIVITY.PK_RA_ID, T_ISSUE.PK_ISSUE_ID
FROM T_ACTIVITY, T_ISSUE, T_ISSUE_LNK
WHERE
T_ACTIVITY.FK_RO_ID=T_ISSUE_LNK.FK_RO_ID AND
T_ISSUE_LNK.FK_ISSUE_ID=T_ISSUE.PK_ISSUE_ID;

ALTER TABLE T_ACTIVITY DROP COLUMN NEXT_DEADLINE_PLUS;
ALTER TABLE T_ACTIVITY CHANGE COLUMN NEXT_REPORTING DATE_COMMENTS VARCHAR(255);
ALTER TABLE T_ACTIVITY ADD COLUMN NEXT_REPORTING VARCHAR(255);
ALTER TABLE T_ACTIVITY ADD COLUMN VALID_TO DATE;

CREATE TABLE T_HELP (
  PK_HELP_ID VARCHAR(50) NOT NULL PRIMARY KEY,
  HELP_TITLE VARCHAR(255),
  HELP_TEXT TEXT
);


DROP TABLE IF EXISTS T_ISSUER;
CREATE TABLE T_ISSUER (
  PK_ISSUER_ID int(10) unsigned NOT NULL auto_increment,
  ISSUER_NAME varchar(255) default NULL,
	ISSUER_ACRONYM varchar(100),
	ISSUER_URL VARCHAR(255) default NULL,
	ISSUER_ADDRESS varchar (255) default NULL,
	ISSUER_EMAIL varchar (255) default NULL,
	DESCRIPTION text,
  PRIMARY KEY  (PK_ISSUER_ID) );

UPDATE T_SOURCE SET ISSUED_BY = '' WHERE ISSUED_BY IS NULL;

INSERT INTO T_ISSUER ( ISSUER_NAME ) SELECT DISTINCT ISSUED_BY FROM T_SOURCE;

DROP TABLE IF EXISTS T_SOURCE_TMP;
CREATE TABLE T_SOURCE_TMP (
  PK_SOURCE_ID int(10) unsigned NOT NULL ,
  FK_TYPE_ID int(10) unsigned NOT NULL ,
  SOURCE_CODE varchar(50) NOT NULL default '',
  TITLE text NOT NULL,
  CELEX_REF varchar(255) default NULL,
  LEGAL_NAME varchar(255) NOT NULL default '',
  URL varchar(255) default NULL,
  SECRETARIAT text,
  SECRETARIAT_URL varchar(255) default NULL,
  ABSTRACT text,
  EC_ACCESSION date default NULL,
  EC_ENTRY_INTO_FORCE date default NULL,
  DRAFT enum('Y','N') default NULL,
  COMMENT text,
  LAST_MODIFIED date default NULL,
  ISSUED_BY text,
  VALID_FROM date default NULL,
  ALIAS text,
  ISSUED_BY_URL varchar(255) default NULL,
	FK_ISSUER_ID INT(10),
  PRIMARY KEY  (PK_SOURCE_ID) 
) TYPE=MyISAM;

INSERT INTO T_SOURCE_TMP (pk_source_id, fk_type_id, source_code, title, celex_ref, legal_name, url, secretariat,
  secretariat_url, abstract, ec_accession, ec_entry_into_force, draft, comment, last_modified, issued_by, valid_from, 
	alias, issued_by_url, fk_issuer_id) SELECT s.pk_source_id, s.fk_type_id, s.source_code, s.title, s.celex_ref, s.legal_name, 
	s.url, s.secretariat,  s.secretariat_url, s.abstract, s.ec_accession, s.ec_entry_into_force, s.draft, 
	s.comment, s.last_modified, s.issued_by, s.valid_from, 
	s.alias, s.issued_by_url, i.PK_ISSUER_ID  FROM T_SOURCE s, T_ISSUER i WHERE s.ISSUED_BY = i.ISSUER_NAME;

ALTER TABLE T_SOURCE add FK_ISSUER_ID int(10);

DELETE FROM T_SOURCE;

INSERT INTO T_SOURCE (pk_source_id, fk_type_id, source_code, title, celex_ref, legal_name, url, secretariat,
  secretariat_url, abstract, ec_accession, ec_entry_into_force, draft, comment, last_modified, issued_by, valid_from, 
	alias, issued_by_url, fk_issuer_id) SELECT s.pk_source_id, s.fk_type_id, s.source_code, s.title, s.celex_ref, s.legal_name, 
	s.url, s.secretariat,  s.secretariat_url, s.abstract, s.ec_accession, s.ec_entry_into_force, s.draft, 
	s.comment, s.last_modified, s.issued_by, s.valid_from, 
	s.alias, s.issued_by_url, s.fk_ISSUER_ID  FROM T_SOURCE_TMP s;

DROP TABLE T_SOURCE_TMP;

