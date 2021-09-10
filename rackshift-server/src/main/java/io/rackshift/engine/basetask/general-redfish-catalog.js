//Copyright 2017, Dell EMC, Inc.
'use strict';

module.exports = {
    friendlyName: 'General Redfish Catalog Job',
    injectableName: 'Task.Base.General.Redfish.Catalog',
    runJob: 'Job.General.Redfish.Catalog',
    requiredOptions: [],
    requiredProperties: {},
    properties: {
        catalog: {}
    }
};
