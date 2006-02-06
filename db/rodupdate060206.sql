ALTER TABLE T_OBLIGATION DROP VERSION;
ALTER TABLE T_OBLIGATION DROP PARENT_OBLIGATION;
ALTER TABLE T_OBLIGATION DROP HAS_NEWER_VERSION;

create table T_UNDO(
	undo_time bigint,
	tab char(32),
	col char(32),
	operation enum('I','D','U'),
	quotes enum('n','y'),
	p_key enum('n','y'),
	value blob,
	sub_trans_nr int default '0',
	show_object enum('n','y'),
	primary key(undo_time,tab,col,sub_trans_nr));
