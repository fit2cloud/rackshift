delete from user where status = 0;
alter table bare_metal_rule add column config bit default 0 comment '额外配置 json';