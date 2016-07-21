	
alter table users add full_name varchar(250);
update users set full_name=name;
