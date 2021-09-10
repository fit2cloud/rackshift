//Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'UCS Service Profile Discovery Job',
    injectableName: 'Task.Base.UCS.Service.Profile.Discovery',
    runJob: 'Job.Ucs.Service.Profile.Discovery',
    requiredOptions: [
        'uri'
    ],
    requiredProperties: {
    },
    properties: {
        catalog: {}
    }
};
