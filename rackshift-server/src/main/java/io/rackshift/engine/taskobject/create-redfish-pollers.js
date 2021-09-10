// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create Default Pollers',
    injectableName: 'Task.Pollers.Redfish.CreateDefault',
    implementsTask: 'Task.Base.Pollers.Redfish.CreateDefault',
    options: {
        nodeId: null,
        pollers: []
    },
    properties: {}
};
