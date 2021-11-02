// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog megaraid',
    injectableName: 'Task.Catalog.megaraid',
    implementsTask: 'Task.Base.Linux.Catalog',
    optionsSchema: 'catalog-raid.json',
    options: {
        adapter: '0',
        path: '/opt/MegaRAID/storcli/storcli64',
        commands: [
            'sudo {{options.path}} /c{{ options.adapter }} show all J',
            'sudo {{options.path}} show ctrlcount J',
            'sudo {{options.path}} /c{{ options.adapter }} /eall /sall show all J',
            'sudo {{options.path}} /c{{ options.adapter }} /vall show all J'
        ]
    },
    properties: {
        catalog: {
            type: 'megaraid'
        }
    }
};