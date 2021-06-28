insert into workflow
values (17,
        'system',
        'Graph.Raid.Catalog.AdaptecRAID',
        '搜集New H3C Technologies Co., Ltd.服务器磁盘Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'New H3C Technologies Co., Ltd.\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (18,
        'system',
        'Graph.Raid.Delete.AdaptecRAID',
        '清空New H3C Technologies Co., Ltd.服务器磁盘和Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'New H3C Technologies Co., Ltd.\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (19,
        'system',
        'Graph.Raid.Create.AdaptecRAID',
        '创建New H3C Technologies Co., Ltd.服务器磁盘Raid虚拟磁盘',
        'POST_OTHER_WORKFLOW_START',
        '[\'New H3C Technologies Co., Ltd.\']',
        'true',
        null,
        'enable',
        now());
