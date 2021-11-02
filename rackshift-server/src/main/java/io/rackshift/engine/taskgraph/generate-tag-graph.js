// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Generate Tag',
    injectableName: 'Graph.GenerateTags',
    options: {
        "generate-tag": {
            nodeIds: null
        }
    },
    tasks: [
        {
            label: 'generate-tag',
            taskName: 'Task.Catalog.GenerateTag'
        }
    ]
};

