// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Generate SKU',
    injectableName: 'Graph.GenerateSku',
    tasks: [
        {
            label: 'generate-sku',
            taskName: 'Task.Catalog.GenerateSku'
        }
    ]
};

