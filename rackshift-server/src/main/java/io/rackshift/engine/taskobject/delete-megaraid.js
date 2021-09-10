// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Delete RAID via Storcli',
    injectableName: 'Task.Raid.Delete.MegaRAID',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'delete-megaraid.json',
    options: {
        deleteAll: true,
        controller: 0,
        raidIds: [], //[0,1,2]
        path: '/opt/MegaRAID/storcli/storcli64',
        commands: [
            '{{#options.deleteAll}}' +
                'sudo {{options.path}} /c{{options.controller}}/vall del force' +
            '{{/options.deleteAll}}' +
            '{{^options.deleteAll}}{{#options.raidIds}}' +
                'sudo {{options.path}} /c{{options.controller}}/v{{.}} del force;' +
            '{{/options.raidIds}}{{/options.deleteAll}}'
        ]
    },
    properties: {}
};
