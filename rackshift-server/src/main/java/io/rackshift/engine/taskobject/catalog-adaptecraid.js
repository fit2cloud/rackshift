// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog adaptecraid',
    injectableName: 'Task.Catalog.adaptecraid',
    implementsTask: 'Task.Base.Linux.Catalog',
    optionsSchema: 'catalog-raid.json',
    options: {
        adapter: '0',
        commands: [
            'sudo /usr/Arcconf/arcconf getconfig 1 al'
        ]
    },
    properties: {
        catalog: {
            type: 'adaptecraid'
        }
    }
};
