// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Base Arista Zerotouch',
    injectableName: 'Task.Base.Arista.Zerotouch',
    runJob: 'Job.Arista.Zerotouch',
    requiredOptions: [
        'eosImage',
        'startupConfig',
        'bootConfig',
        'bootfile',
    ],
    requiredProperties: {},
    properties: {
        os: {
            switch: {
                type: 'eos'
            }
        }
    }
};
