create table T_HISTORIC_DEADLINES (
	FK_RA_ID int(10), 
	DEADLINE date,
	primary key(FK_RA_ID, DEADLINE));
