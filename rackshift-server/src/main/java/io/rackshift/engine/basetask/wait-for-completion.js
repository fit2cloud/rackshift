// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Base Completion URI Trigger',
    injectableName: 'Task.Base.Wait.Completion.Uri',
    runJob: 'Job.Wait.Completion.Uri',
    optionsSchema: {
        properties: {
            completionUri: {
                type: 'string',
                description: 'The url pattern that indicates the wait uri task has finsihed',
                minLength: 1
            }
        },
        required: [ 'completionUri' ]
    },
    requiredProperties: {},
    properties: {}
};
