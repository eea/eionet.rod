ALTER TABLE T_OBLIGATION ADD DATA_USED_FOR_URL varchar(255);
UPDATE T_OBLIGATION SET DATA_USED_FOR_URL = DATA_USED_FOR;