// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create Default Redfish Pollers',
    injectableName: 'Task.Base.Pollers.Redfish.CreateDefault',
    runJob: 'Job.Pollers.CreateDefault',
    requiredOptions: [
        'pollers'
    ],
    requiredProperties: {},
    properties: {}
};
