insert into workflow
values (5,
        'system',
        'Graph.Raid.Catalog.AdaptecRAID',
        '搜集Inspur服务器磁盘Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'Inspur\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (6,
        'system',
        'Graph.Raid.Delete.AdaptecRAID',
        '清空Inspur服务器磁盘和Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'Inspur\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (7,
        'system',
        'Graph.Raid.Create.AdaptecRAID',
        '创建Inspur服务器磁盘Raid虚拟磁盘',
        'POST_OTHER_WORKFLOW_START',
        '[\'Inspur\']',
        'true',
        null,
        'enable',
        now());