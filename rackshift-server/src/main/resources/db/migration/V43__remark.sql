alter table bare_metal add remark varchar(1024);
update system_parameter set param_value = 'registry.cn-qingdao.aliyuncs.com/x-lab/kvm:v1.7.1' where param_key = 'kvm.image';