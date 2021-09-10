// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'WSMAN Poller Service',
    injectableName: 'Graph.Service.Poller.Wsman',
    serviceGraph: true,
    
    options: {
        defaults: {
            serviceId: null,
            nodeId:null
            
                    
        }
    },
    
    
    tasks: [
            {
             label: 'wsman',
             taskDefinition: {
                friendlyName: 'Wsman requester',
                injectableName: 'Task.Inline.Wsman',
                implementsTask: 'Task.Base.Wsman',
                options: {
                	      serviceId : null,
                	      nodeId: null
                	},
                properties: {}
            }
        },
    ]
};
