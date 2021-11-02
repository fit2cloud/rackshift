// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create RAID via Hpssacli',
    injectableName: 'Task.Raid.Create.HpssaRaid',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'create-hpssaraid.json',
    options: {
        createDefault: false,
        controller: 0,
        raidList: [],
        /*[
            {
                
                'type': 'raid1',
                'drives': [1, 4]
            }
        ],*/
        path: '/usr/sbin/hpssacli',
        // create 1 logicaldrive max 1
        commands: [
            '{{#options.createDefault}}' +
                'sudo {{options.path}} create {{options.controller}} logicaldrive max 0 0 0 noprompt' +
            '{{/options.createDefault}}' +
            '{{^options.createDefault}}{{#options.raidList}}' +
                'sudo {{options.path}} ctrl slot={{options.controller}} create type=ld drives={{drives}} raid={{type}};'+
            '{{/options.raidList}}{{/options.createDefault}}'
        ]
    },
    properties: {}
};
