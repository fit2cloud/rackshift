// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Redfish Discovery Job',
    injectableName: 'Task.Base.Redfish.Discovery',
    runJob: 'Job.Redfish.Discovery',
    requiredOptions: [
        'uri'
    ],
    requiredProperties: {
    },
    properties: {
        catalog: {}
    }
};
