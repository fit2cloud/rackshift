// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'EMC Redfish Catalog Job',
    injectableName: 'Task.Base.Emc.Redfish.Catalog',
    runJob: 'Job.Emc.Redfish.Catalog',
    requiredOptions: [],
    requiredProperties: {},
    properties: {
        catalog: {}
    }
};
