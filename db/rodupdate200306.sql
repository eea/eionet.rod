ALTER TABLE T_UNDO CHANGE operation operation enum('I','D','U','A','L','K','O');

ALTER TABLE T_UNDO DROP PRIMARY KEY;
ALTER TABLE T_UNDO ADD PRIMARY KEY (undo_time, tab,col,operation,sub_trans_nr);