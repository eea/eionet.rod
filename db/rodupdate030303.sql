ALTER TABLE T_ACTIVITY ADD COLUMN NEXT_DEADLINE2 DATE;

CREATE TABLE T_TEMP (MAX_ID int(10));

INSERT INTO T_TEMP SELECT MAX(PK_CLIENT_ID) FROM T_CLIENT;

INSERT INTO T_CLIENT (PK_CLIENT_ID, CLIENT_NAME, CLIENT_ACRONYM, CLIENT_URL, CLIENT_EMAIL, CLIENT_ADDRESS, DESCRIPTION) 	SELECT i.PK_ISSUER_ID + t.MAX_ID, i.ISSUER_NAME, i.ISSUER_ACRONYM, i.ISSUER_URL, i.ISSUER_EMAIL, i.ISSUER_ADDRESS, i.DESCRIPTION FROM T_ISSUER i, T_TEMP t;

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
	FK_CLIENT_ID INT(10),
  PRIMARY KEY  (PK_SOURCE_ID) 
) TYPE=MyISAM;


INSERT INTO T_SOURCE_TMP (pk_source_id, fk_type_id, source_code, title, celex_ref, legal_name, url, secretariat,
  secretariat_url, abstract, ec_accession, ec_entry_into_force, draft, comment, last_modified, issued_by, valid_from, 
	alias, issued_by_url, fk_client_id) SELECT s.pk_source_id, s.fk_type_id, s.source_code, s.title, s.celex_ref, s.legal_name, 
	s.url, s.secretariat,  s.secretariat_url, s.abstract, s.ec_accession, s.ec_entry_into_force, s.draft, 
	s.comment, s.last_modified, s.issued_by, s.valid_from, 
	s.alias, s.issued_by_url, s.FK_ISSUER_ID + t.MAX_ID  FROM T_SOURCE s, T_TEMP t;


ALTER TABLE T_SOURCE DROP FK_ISSUER_ID;
ALTER TABLE T_SOURCE ADD FK_CLIENT_ID INT (10);
DELETE FROM T_SOURCE;

INSERT INTO T_SOURCE (pk_source_id, fk_type_id, source_code, title, celex_ref, legal_name, url, secretariat,
  secretariat_url, abstract, ec_accession, ec_entry_into_force, draft, comment, last_modified, issued_by, valid_from, 
	alias, issued_by_url, fk_client_id) SELECT s.pk_source_id, s.fk_type_id, s.source_code, s.title, s.celex_ref, s.legal_name, 
	s.url, s.secretariat,  s.secretariat_url, s.abstract, s.ec_accession, s.ec_entry_into_force, s.draft, 
	s.comment, s.last_modified, s.issued_by, s.valid_from, 
	s.alias, s.issued_by_url, s.fk_client_ID  FROM T_SOURCE_TMP s;


DROP TABLE T_TEMP;

DROP TABLE T_SOURCE_TMP;

ALTER TABLE T_CLIENT ADD POSTAL_CODE VARCHAR(100);
ALTER TABLE T_CLIENT ADD CITY VARCHAR(255);
ALTER TABLE T_CLIENT ADD FK_SPATIAL_ID INT(100);

ALTER TABLE T_SOURCE ADD LAST_UPDATE TIMESTAMP;

ALTER TABLE T_SOURCE ADD RM_NEXT_UPDATE DATE;
ALTER TABLE T_SOURCE ADD RM_VERIFIED DATE;
ALTER TABLE T_SOURCE ADD RM_VERIFIED_BY VARCHAR(50);

ALTER TABLE T_REPORTING ADD RM_NEXT_UPDATE DATE;
ALTER TABLE T_REPORTING ADD RM_VERIFIED DATE;
ALTER TABLE T_REPORTING ADD RM_VERIFIED_BY VARCHAR(50);

ALTER TABLE T_ACTIVITY ADD RM_NEXT_UPDATE DATE;
ALTER TABLE T_ACTIVITY ADD RM_VERIFIED DATE;
ALTER TABLE T_ACTIVITY ADD RM_VERIFIED_BY VARCHAR(50);


UPDATE T_SOURCE_CLASS SET CLASS_NAME="Other legislation" WHERE PK_CLASS_ID=23;

DELETE FROM T_SOURCE_CLASS WHERE PK_CLASS_ID=18;
DELETE FROM T_SOURCE_LNK WHERE PARENT_TYPE='C' AND FK_SOURCE_PARENT_ID=18;
DELETE FROM T_SOURCE_CLASS WHERE PK_CLASS_ID=19;
DELETE FROM T_SOURCE_LNK WHERE PARENT_TYPE='C' AND FK_SOURCE_PARENT_ID=19;
DELETE FROM T_SOURCE_CLASS WHERE PK_CLASS_ID=21;
DELETE FROM T_SOURCE_LNK WHERE PARENT_TYPE='C' AND FK_SOURCE_PARENT_ID=21;
DELETE FROM T_SOURCE_CLASS WHERE PK_CLASS_ID=22;
DELETE FROM T_SOURCE_LNK WHERE PARENT_TYPE='C' AND FK_SOURCE_PARENT_ID=22;
DELETE FROM T_SOURCE_CLASS WHERE PK_CLASS_ID=24;
DELETE FROM T_SOURCE_LNK WHERE PARENT_TYPE='C' AND FK_SOURCE_PARENT_ID=24;





