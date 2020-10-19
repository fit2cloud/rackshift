insert into workflow
values (8,
        'system',
        'Graph.HP.ssacli.Catalog',
        '搜集HPE服务器磁盘Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'HP\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (9,
        'system',
        'Graph.Raid.Delete.HpssaRAID',
        '清空HPE服务器磁盘和Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'HP\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (10,
        'system',
        'Graph.Raid.Create.HpssaRAID',
        '创建HPE服务器磁盘Raid虚拟磁盘',
        'POST_OTHER_WORKFLOW_START',
        '[\'HP\']',
        'true',
        null,
        'enable',
        now());
