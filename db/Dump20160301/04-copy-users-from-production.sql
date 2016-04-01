
insert into cursus2.users 
	(role, email, status, avatar_url, first_name, last_name, name, personal_info, created_at, updated_at, old_id) 
select 
	role, email, status, avatar_url, first_name, last_name, concat(first_name, ' ', last_name), personal_appeal, created_at, now(), id 
from cursus_production.users 
where cursus_production.users.email not in (select email from cursus2.users);
