// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create Default Pollers',
    injectableName: 'Task.Pollers.CreateDefault',
    implementsTask: 'Task.Base.Pollers.CreateDefault',
    options: {
        nodeId: null,
        pollers: []
    },
    properties: {}
};
