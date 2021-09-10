// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell WSMAN Downlaod Catalog Graph',
    injectableName: 'Graph.Dell.Wsman.Download.Catalog',
    options: {
        defaults: {
            fileName: null,
            fileURL: null,
            targetLocation: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-downaload-catalog',
            taskName: 'Task.Dell.Wsman.Download.Catalog'
        }
    ]
};

