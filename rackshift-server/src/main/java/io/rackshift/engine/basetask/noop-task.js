// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'base-noop',
    injectableName: 'Task.Base.noop',
    runJob: 'Job.noop',
    optionsSchema: {
        properties: {
            delay: {
                description: 'Specify the time span that this task will be finished after given time, in milliseconds', //jshint ignore: line
                type: 'integer',
                minimum: 0
            },
            option1: {
                description: 'A placeholder testing option #1'
            },
            option2: {
                description: 'A placeholder testing option #2'
            },
            options3: {
                description: 'A placeholder testing option #3'
            }
        }
    },
    requiredProperties: {},
    properties: {
        noop: {
            type: 'null'
        }
    }
};
