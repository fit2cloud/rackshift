// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create RAID via Arcconf',
    injectableName: 'Task.Raid.Create.AdaptecRAID',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'create-adaptecraid.json',
    options: {
        createDefault: true,
        controller: 0,
        raidList: [],
        /*[
            {
                'enclosure': 255,
                'type': 'raid1',
                'drives': [1, 4],
                'name': 'VD1'
            },
            {
                'enclosure': 255,
                'type': 'raid5',
                'drives': [2, 5, 3],
                'name': 'VD2'
            },
            {
                'enclosure': 255,
                'type': 'raid10',
                'drives': [6, 7, 8, 9],
                'name': 'VD3',
                'size': '200G',
                'drivePerArray': 2
            },
            {
                'enclosure': 255,
                'type': 'raid10',
                'drives': [6, 7, 8, 9],
                'name': 'VD4',
                'size': '800G',
                'drivePerArray': 2
            }
        ],*/
        path: '/usr/Arcconf/arcconf',
        // create 1 logicaldrive max 1
        commands: [
            '{{#options.createDefault}}' +
                'sudo {{options.path}} create {{options.controller}} logicaldrive max 0 0 0 noprompt' +
            '{{/options.createDefault}}' +
            '{{^options.createDefault}}{{#options.raidList}}' +
                'sudo {{options.path}} create {{options.controller}} logicaldrive' 
                +'{{#name}}name {{name}}{{/name}} max {{type}} {{enclosure}} {{drives}} noprompt;' +
            '{{/options.raidList}}{{/options.createDefault}}'
        ]
    },
    properties: {}
};
