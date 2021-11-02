// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Configure Idrac',
    injectableName: 'Task.Base.Dell.Wsman.Configure.Idrac',
    runJob: 'Job.Dell.Wsman.Configure.Idrac',
    requiredOptions: [
        "gateway",
        "netmask",
        "address"
    ],
    requiredProperties: {},
    properties:{}
};
