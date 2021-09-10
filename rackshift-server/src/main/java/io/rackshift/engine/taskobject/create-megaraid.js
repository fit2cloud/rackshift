// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create RAID via Storcli',
    injectableName: 'Task.Raid.Create.MegaRAID',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'create-megaraid.json',
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
        path: '/opt/MegaRAID/storcli/storcli64',
        commands: [
            '{{#options.createDefault}}' +
                'sudo {{options.path}} /c{{options.controller}} add vd each type=raid0' +
            '{{/options.createDefault}}' +
            '{{^options.createDefault}}{{#options.raidList}}' +
                'sudo {{options.path}} /c{{options.controller}} add vd type={{type}}' +
                    '{{#name}} name={{name}}{{/name}}{{#size}} size={{size}}{{/size}} ' +
                    'drives={{enclosure}}:{{drives}}' +
                    '{{#drivePerArray}} PDPerArray={{drivePerArray}}{{/drivePerArray}};' +
            '{{/options.raidList}}{{/options.createDefault}}'
        ]
    },
    properties: {}
};
