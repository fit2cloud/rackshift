// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Delete RAID via Hpssacli',
    injectableName: 'Task.Raid.Delete.HpssaRaid',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'delete-megaraid.json',
    options: {
        deleteAll: true,
        controller: 0,
        raidIds: [], //[0,1,2]
        path: '/usr/sbin/hpssacli',
        commands: [
            '{{#options.deleteAll}}' +
                'sudo {{options.path}} ctrl slot={{options.controller}} ld all delete forced' +
            '{{/options.deleteAll}}' +
            '{{^options.deleteAll}}{{#options.raidIds}}' +
                'sudo {{options.path}} /c{{options.controller}}/v{{.}} del force;' +
            '{{/options.raidIds}}{{/options.deleteAll}}'
        ]
    },
    properties: {}
};
