CREATE TABLE T_RASPATIAL_LNK (
  FK_SPATIAL_ID INT(10) UNSIGNED NOT NULL DEFAULT 0,
  FK_RA_ID INT(10) UNSIGNED NOT NULL DEFAULT 0,
	VOLUNTARY ENUM('Y', 'N') DEFAULT 'N',
  PRIMARY KEY  (FK_SPATIAL_ID,FK_RA_ID)
);

INSERT INTO T_RASPATIAL_LNK (FK_RA_ID, FK_SPATIAL_ID, VOLUNTARY)
SELECT DISTINCT a.PK_RA_ID, s.PK_SPATIAL_ID, sl.VOLUNTARY 
FROM T_ACTIVITY a, T_SPATIAL s, T_SPATIAL_LNK sl
WHERE
a.FK_RO_ID=sl.FK_RO_ID AND
sl.FK_SPATIAL_ID=s.PK_SPATIAL_ID;


CREATE TABLE T_SPATIAL_HISTORY (
  FK_SPATIAL_ID INT(10) UNSIGNED NOT NULL DEFAULT 0,
  FK_RA_ID INT(10) UNSIGNED NOT NULL DEFAULT 0,
	VOLUNTARY ENUM('Y', 'N') DEFAULT 'N',
	START_DATE DATE,
	END_DATE DATE
);


INSERT INTO T_SPATIAL_HISTORY (FK_RA_ID, FK_SPATIAL_ID, VOLUNTARY, START_DATE)
SELECT DISTINCT a.PK_RA_ID, s.PK_SPATIAL_ID, sl.VOLUNTARY, NOW()   
FROM T_ACTIVITY a, T_SPATIAL s, T_SPATIAL_LNK sl
WHERE
a.FK_RO_ID=sl.FK_RO_ID AND
sl.FK_SPATIAL_ID=s.PK_SPATIAL_ID;


UPDATE T_HELP SET PK_HELP_ID="HELP_RA_SPATIALCOVERAGE" WHERE PK_HELP_ID="HELP_RO_SPATIALCOVERAGE";
UPDATE T_HELP SET PK_HELP_ID="HELP_RA_VOLUNTARYCOUNTRIES" WHERE PK_HELP_ID="HELP_RO_VOLUNTARYCOUNTRIES";

ALTER TABLE T_ACTIVITY ADD LOCATION_PTR VARCHAR(255);
ALTER TABLE T_ACTIVITY ADD LOCATION_INFO VARCHAR(255);

INSERT INTO T_LOOKUP (C_TERM, CATEGORY, C_VALUE) VALUES ("Voluntary request", 2, "V");
ALTER TABLE T_REPORTING MODIFY LEGAL_MORAL ENUM("L", "M", "V") NOT NULL;