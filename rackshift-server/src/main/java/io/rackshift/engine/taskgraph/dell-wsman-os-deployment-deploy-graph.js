// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell wsman OS Deploy ISO Graph',
    injectableName: 'Graph.Dell.Wsman.Os.Deploy',
    options: {
        defaults: {
           
            hypervisorType: null,
            hypervisorVersion: null,
            
            isoFileShare: {
               address: null,
               description: null,
               fileName: null,
               name: null,
               path: null,
               scriptDirectory: null,
               scriptName: null,
               type: null,
               passwordCredential: {
                   domain: null,
                   password: null,
                   username: null
                 },
             },
             serverAddress: null,
             userName: null,
             password: null
           }    
        },
    tasks: [
         {
            label: 'dell-wsman-os-deploy',
            taskName: 'Task.Dell.Wsman.Os.Deploy'
        }
    ]
   
};
