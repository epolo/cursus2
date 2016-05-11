
alter table disciplines add uuid char(36), add index(uuid);
update disciplines set uuid=uuid() where uuid is null;

alter table courses add uuid char(36), add index(uuid);
update courses set uuid=uuid() where uuid is null;

alter table users add uuid char(36), add index(uuid);
update users set uuid=uuid() where uuid is null;
