insert into workflow
values (1,
        'system',
        'Graph.InstallCentOS',
        '安装 Centos7 64位版',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\']',
        'true',
        null,
        'enable',
        now());

insert into workflow
values (2,
        'system',
        'Graph.Dell.perccli.Catalog',
        '搜集Dell服务器磁盘Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'DELL\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (3,
        'system',
        'Graph.Raid.Delete.MegaRAID',
        '清空Dell服务器磁盘和Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'DELL\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (4,
        'system',
        'Graph.Raid.Create.PercRAID',
        '创建Dell服务器磁盘Raid虚拟磁盘',
        'POST_OTHER_WORKFLOW_START',
        '[\'DELL\']',
        'true',
        null,
        'enable',
        now());


update workflow set default_params = '{
        "options": {
          "defaults": {
            "version": "7",
            "repo": null,
            "rootPassword": "RackShift",
            "hostname": "rackshift-node",
            "networkDevices": [
              {
                "device": null,
                "ipv4": {
                  "ipAddr": "192.168.1.10",
                  "gateway": "192.168.1.1",
                  "netmask": "255.255.255.0"
                }
              }
            ],
            "installDisk": "/dev/sda",
            "installPartitions": [
              {
                "mountPoint": "/",
                "size": "auto",
                "fsType": "ext3"
              },
              {
                "mountPoint": "swap",
                "size": "4096",
                "fsType": "swap"
              },
              {
                "mountPoint": "/boot",
                "size": "4096",
                "fsType": "ext3"
              },
              {
                "mountPoint": "biosboot",
                "size": "1",
                "fsType": "biosboot"
              }
            ]
          }
        }
      }' where injectable_name = 'Graph.InstallCentOS';


update workflow set default_params = '{
        "options": {
          "bootstrap-rancher": {
            "dockerFile": "secure.erase.docker.tar.xz"
          },
          "create-raid": {
            "createDefault": false,
            "controller": 0,
            "path": "/opt/MegaRAID/perccli/perccli64",
            "raidList": [
              {
                "enclosure": 32,
                "type": null,
                "drives": [],
                "name": "VD0"
              }
            ]
          }
        }
      }' where injectable_name = 'Graph.Raid.Create.PercRAID';



INSERT INTO USER
VALUES

(
	'admin',
	'admin',
	'admin@test.com',
	'e10adc3949ba59abbe56e057f20f883e',
	1,
	1599554996261,
	1599554996261,
	NULL,
	NULL,
	NULL,
	NULL
);

INSERT INTO user_role
VALUES
	(
		1,
		'admin',
		1,
		NULL,
		1599554996261,
		1599554996261
	);

INSERT INTO role
VALUES
	(
		1,
		'admin',
		NULL,
		'admin',
		1599554996261,
		1599554996261
	);

