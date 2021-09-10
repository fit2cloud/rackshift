// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'UCS Poller',
    injectableName: 'Graph.UCS.Poller',
    options: {
        defaults: {
            graphOptions: {
                target: null
            },
            nodeId: null,
            nodeIds: [ "{{ options.nodeId }}" ]
        }
    },
    tasks: [
        {
            label: 'create-ucs-pollers',
            taskDefinition: {
                friendlyName: 'Create Default Pollers',
                injectableName: 'Task.Inline.Pollers.CreateDefault',
                implementsTask: 'Task.Base.Pollers.CreateDefault',
                properties: {},
                options: {
                    nodeId: null,
                    pollers: [
                        {
                            "type": "ucs",
                            "pollInterval": 60000,
                            "config": {
                                "command": "ucs.powerthermal"
                            }
                        },
                        {
                            "type": "ucs",
                            "pollInterval": 60000,
                            "config": {
                                "command": "ucs.fan"
                            }
                        },
                        {
                            "type": "ucs",
                            "pollInterval": 60000,
                            "config": {
                                "command": "ucs.psu"
                            }
                        },
                        {
                            "type": "ucs",
                            "pollInterval": 60000,
                            "config": {
                                "command": "ucs.disk"
                            }
                        },
                        {
                            "type": "ucs",
                            "pollInterval": 60000,
                            "config": {
                                "command": "ucs.led"
                            }
                        },
                        {
                            "type": "ucs",
                            "pollInterval": 60000,
                            "config": {
                                "command": "ucs.sel"
                            }
                        }
                    ]
                }
            }
        }
    ]
};
