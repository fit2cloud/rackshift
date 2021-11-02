// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Redfish Discovery List Job',
    injectableName: 'Task.Base.Redfish.Discovery.List',
    runJob: 'Job.Redfish.Discovery.List',
    // requiredOptions: [
    //     'uri'
    // ],
    requiredProperties: {
    },
    properties: {
        catalog: {}
    }
};
