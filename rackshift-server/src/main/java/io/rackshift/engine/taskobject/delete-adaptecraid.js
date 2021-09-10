// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Delete RAID via Arcconf',
    injectableName: 'Task.Raid.Delete.AdaptecRAID',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'delete-megaraid.json',
    options: {
        deleteAll: true,
        controller: 1,
        raidIds: [], //[0,1,2]
        path: '/usr/Arcconf/arcconf',
        commands: [
            '{{#options.deleteAll}}' +
                'sudo {{options.path}} delete {{options.controller}} logicaldrive all noprompt' +
            '{{/options.deleteAll}}' +
            '{{^options.deleteAll}}{{#options.raidIds}}' +
                'sudo {{options.path}} /c{{options.controller}}/v{{.}} del force;' +
            '{{/options.raidIds}}{{/options.deleteAll}}'
        ]
    },
    properties: {}
};
