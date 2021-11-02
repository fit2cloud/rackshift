// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Provide Catalog Data Values',
    injectableName: 'Task.Base.Catalogs.ProvideValue',
    runJob: 'Job.Catalogs.ProvideData',
    optionsSchema: {
        properties: {
            source: {
                description: 'The name of catalog source',
                type: 'string',
                minLength: 1
            },
            path: {
                description: 'The json path for the catalog data',
                type: 'string',
                minLength: 1
            }
        },
        required: ['source', 'path']
    },
    requiredProperties: {},
    properties: {
        context: {}
    }
};
