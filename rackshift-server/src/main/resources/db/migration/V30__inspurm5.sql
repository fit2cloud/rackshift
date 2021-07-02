
insert into workflow
values (21,
        'system',
        'Graph.Quanta.storcli.Catalog',
        '搜集 InspurM5 服务器磁盘 Raid 信息',
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
values (22,
        'system',
        'Graph.Raid.Delete.MegaRAID',
        '清空 InspurM5 服务器磁盘 Raid 信息',
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
values (23,
        'system',
        'Graph.Raid.Create.PercRAID',
        '创建 InspurM5 服务器磁盘 Raid 虚拟磁盘',
        'POST_OTHER_WORKFLOW_START',
        '[\'Inspur\']',
        'true',
        '{
  "options": {
    "bootstrap-rancher": {
      "dockerFile": "secure.erase.docker.tar.xz"
    },
    "create-raid": {
      "createDefault": false,
      "controller": 0,
      "path": "/opt/MegaRAID/storcli/storcli64",
      "raidList": [
        {
          "enclosure": 32,
          "type": "raid5",
          "drives": [
            0,
            1,
            2
          ],
          "name": "VD0"
        }
      ]
    }
  }
}',
        'enable',
        now());
