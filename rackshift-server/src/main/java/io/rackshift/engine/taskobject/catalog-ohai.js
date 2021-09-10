// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog ohai',
    injectableName: 'Task.Catalog.ohai',
    implementsTask: 'Task.Base.Linux.Catalog',
    options: {
        commands: [
            'sudo ohai --directory /etc/ohai/plugins'
        ]
    },
    properties: {
        catalog: {
            type: 'ohai'
        }
    }
};
