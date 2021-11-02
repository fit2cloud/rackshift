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
            'sudo {{options.path}} /c{{options.controller}}/vall del force'
        ]
    },
    properties: {}
};
