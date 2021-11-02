// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create OBM settings',
    injectableName: 'Task.Base.Obm.Ipmi.CreateSettings',
    runJob: 'Job.Obm.Ipmi.CreateSettings',
    requiredOptions: [
        'user',
        'password'
    ],
    requiredProperties: {},
    properties: {
        obm: {
            type: 'ipmi'
        }
    }
};
