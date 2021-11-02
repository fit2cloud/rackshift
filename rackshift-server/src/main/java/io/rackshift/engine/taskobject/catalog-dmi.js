// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog dmi',
    injectableName: 'Task.Catalog.dmi',
    implementsTask: 'Task.Base.Linux.Catalog',
    options: {
        commands: [
            'sudo dmidecode'
        ]
    },
    properties: {
        catalog: {
            type: 'dmi'
        }
    }
};
