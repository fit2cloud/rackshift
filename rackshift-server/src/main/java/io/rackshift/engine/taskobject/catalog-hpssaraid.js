// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog hpssaraid',
    injectableName: 'Task.Catalog.hpssaraid',
    implementsTask: 'Task.Base.Linux.Catalog',
    optionsSchema: 'catalog-raid.json',
    options: {
        adapter: '0',
        commands: [
            'sudo /usr/sbin/hpssacli ctrl all show config detail'
        ]
    },
    properties: {
        catalog: {
            type: 'hpssaraid'
        }
    }
};
