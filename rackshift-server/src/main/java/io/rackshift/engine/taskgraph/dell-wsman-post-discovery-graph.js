// Copyright 2016, DELL, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Wsman PostDiscovery',
    injectableName: 'Graph.Dell.Wsman.PostDiscovery',
    options: {
    	defaults: {
    		data: null,
			credentials:{
				user: null,
				password: null
			}
    	}
    },
    tasks: [
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory',
            ignoreFailure: true
        },
        {
            label: 'dell-wsman-get-bios',
            taskName: 'Task.Dell.Wsman.GetBios',
            waitOn: {
                'dell-wsman-get-inventory': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'update-lookups',
            taskName: 'Task.Wsman.Update.Lookups',
            waitOn: {
                'dell-wsman-get-bios': 'succeeded'
            },
            ignoreFailure: true
        },
        {
             label: 'create-wsman-pollers',
             taskDefinition: {
                  friendlyName: 'Create Default Pollers',
                  injectableName: 'Task.Inline.Pollers.CreateDefault',
                  implementsTask: 'Task.Base.Pollers.CreateDefault',
                  properties: {},
                  options: {
                      nodeId: null,
                      pollers: [
                          {
                              "type": "wsman",
                              "pollInterval": 30000,
                              "config": {
                                  "command": "powerthermal"
                              }
                          }
                      ]
                  }
             },
             waitOn: {
              'update-lookups': 'finished'
             }
        }
    ]
};
