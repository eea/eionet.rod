DELETE FROM T_LOOKUP WHERE C_TERM="Voluntary request" AND C_VALUE="V";
INSERT INTO T_LOOKUP (C_TERM, CATEGORY, C_VALUE) VALUES ("Voluntary request", "2", "V");

UPDATE T_SPATIAL_HISTORY SET START_DATE="0000-00-00" WHERE START_DATE="2003-11-07";