
alter table cursus2.users add column php_id int;

insert into smf.smf_members (
	member_name, email_address, is_activated, real_name, personal_text, date_registered,
	last_login, member_ip, member_ip2, id_group, hide_email, show_online, pm_email_notify, 
	notify_announcements, notify_regularity, notify_send_body, notify_types, id_theme, id_msg_last_visit, id_post_group,
	buddy_list, message_labels, openid_uri, signature, ignore_boards
	) select 
	concat('cursus-',id),				-- member_name
	email,								-- email_address
	if(strcmp('active',status),0,1),	-- is_activated
	name,								-- real_name
	if(personal_info is null, '', substring(personal_info, 1,	255)),	-- personal_text
	UNIX_TIMESTAMP(created_at),			-- date_registered
	UNIX_TIMESTAMP(),
	'127.0.0.1', '127.0.0.1', 0, 1, 1, 1, 1, 1, 0, 2, 0, 1, 4,
	'', '', '', '', ''
 from cursus2.users 
 where email not in (select email_address from smf.smf_members);

update cursus2.users u set u.php_id=(select id_member from smf.smf_members where email_address=u.email) where u.php_id is null;
