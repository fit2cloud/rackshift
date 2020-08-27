insert into workflow
values (1,
        'Graph.InstallCentOS',
        '安装Centos7 64位版',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\']',
        'true',
        null,
        'enable',
        now());

insert into workflow
values (2,
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
        'Graph.Raid.Create.PercRAID',
        '创建Dell服务器磁盘Raid虚拟磁盘',
        'POST_OTHER_WORKFLOW_START',
        '[\'DELL\']',
        'true',
        null,
        'enable',
        now());

