-- puts ACL_ROWS.STATUS column back into primary key
----------------------------------------------------

alter table ACL_ROWS drop primary key;
alter table ACL_ROWS add primary key (ACL_ID, TYPE, ENTRY_TYPE, PRINCIPAL, STATUS);