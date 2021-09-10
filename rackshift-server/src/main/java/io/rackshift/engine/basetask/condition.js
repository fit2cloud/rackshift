// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Evaluate Condition',
    injectableName: 'Task.Base.Evaluate.Condition',
    runJob: 'Job.Evaluate.Condition',
    optionsSchema: {
        properties: {
            when: {
                description: 'The condition value that to be evaluated. Only string "true" means condition meets', //jshint ignore: line
                type: 'string'
            }
        },
        required: ['when']
    },
    requiredProperties: {},
    properties: {}
};
