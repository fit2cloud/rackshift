// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog perccli',
    injectableName: 'Task.Catalog.perccli',
    implementsTask: 'Task.Base.Linux.Catalog',
    optionsSchema: 'catalog-raid.json',
    options: {
        adapter: '0',
        commands: [
            'sudo /opt/MegaRAID/perccli/perccli64 /c{{ options.adapter }} show all J',
            'sudo /opt/MegaRAID/perccli/perccli64 show ctrlcount J',
            'sudo /opt/MegaRAID/perccli/perccli64 /c{{ options.adapter }} /vall show all J',
            'sudo /opt/MegaRAID/perccli/perccli64 -v'
        ]
    },
    properties: {
        catalog: {
            type: 'perccli'
        }
    }
};
