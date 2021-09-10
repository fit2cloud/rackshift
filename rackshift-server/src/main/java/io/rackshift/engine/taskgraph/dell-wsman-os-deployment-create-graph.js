// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell wsman OS Deployment Create ISO Graph',
    injectableName: 'Graph.Dell.Wsman.Os.Create',
    options: {
        defaults: {
           destinationDir: null,
           destinationFileName: null,
           fileName: null,
           kickStartFileName: null,
           ksLocation: null,
           shareAddress: null,
           sourceDir: null
          }
        },
    tasks: [
         {
            label: 'dell-wsman-os-create',
            taskName: 'Task.Dell.Wsman.Os.Create'
        }
    ]
};
