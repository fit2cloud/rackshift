// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'UCS Discovery Job',
    injectableName: 'Task.Base.UCS.Discovery',
    runJob: 'Job.Ucs.Discovery',
    requiredOptions: [
        'uri'
    ],
    requiredProperties: {
    },
    properties: {
        catalog: {}
    }
};
