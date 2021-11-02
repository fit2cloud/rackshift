// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog flashupdt',
    injectableName: 'Task.Catalog.flashupdt',
    implementsTask: 'Task.Base.Linux.Catalog',
    options: {
        commands: [
            'sudo /opt/intel/flashupdt -i'
        ]
    },
    properties: {
        catalog: {
            type: 'flashupdt'
        }
    }
};
