// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell WSMAN Update System Configuration Components',
    injectableName: 'Graph.Dell.Wsman.UpdateSystemComponents',
    options: {
        defaults: {
            serverIP: null,
            serverUsername: null,
            serverPassword: null,
            shareType: null,
            shareAddress: null,
            shareName: null,
            fileName: null,
            shutdownType: null,
            serverComponents: null,
            cleanup: null,
            forceUpdate: null
        }
    },
    tasks: [
        {
            label: 'dell-wsman-update-systemcomponents',
            taskName: 'Task.Dell.Wsman.UpdateSystemConfigComponents'
        },
        {
            label: 'dell-wsman-get-inventory',
            taskName: 'Task.Dell.Wsman.GetInventory',
            waitOn: {
                'dell-wsman-update-systemcomponents': 'finished'
            }
        },
        {
            label: 'dell-wsman-get-bios',
            taskName: 'Task.Dell.Wsman.GetBios',
            waitOn: {
                'dell-wsman-get-inventory': 'finished'
            }
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
              'dell-wsman-get-bios': 'succeeded'
             }
        }
    ]
};
