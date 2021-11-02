// Copyright 2016, Dell, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman Services Config Job',
    injectableName: 'Task.Base.Dell.Wsman.Config.Services',
    runJob: 'Job.Dell.Wsman.Config',
    requiredOptions: [
         'configServer'
    ],
    requiredProperties: {
    },
    properties: {
        catalog: {}
    }
};
