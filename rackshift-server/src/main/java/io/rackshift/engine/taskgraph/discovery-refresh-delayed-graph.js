// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Refresh node delayed',
    injectableName: 'Graph.Refresh.Delayed.Discovery',
    options: {
        'discovery-refresh-graph': {
            graphOptions: {
                target: null
            },
            nodeId: null
        },
        'generate-sku': {
            nodeId: null
        },
        'generate-enclosure': {
            nodeId: null
        },
        'create-default-pollers': {
            nodeId: null
        },
        'run-sku-graph': {
            nodeId: null
        },
        nodeId: null
    },
    tasks: [
        {
            label: 'discovery-refresh-graph',
            taskDefinition: {
                friendlyName: 'Run Discovery Refresh Graph',
                injectableName: 'Task.Graph.Run.Discovery',
                implementsTask: 'Task.Base.Graph.Run',
                options: {
                    graphName: 'Graph.Discovery',
                    graphOptions: {}
                },
                properties: {}
            },
        },
        {
            label: 'generate-sku',
            waitOn: {
                'discovery-refresh-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateSku',
        },
        {
            label: 'generate-enclosure',
            waitOn: {
                'discovery-refresh-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateEnclosure',
            ignoreFailure: true
        },
        {
            label: 'create-default-pollers',
            taskDefinition: {
                friendlyName: 'Create Default Pollers',
                injectableName: 'Task.Inline.Pollers.CreateDefault',
                implementsTask: 'Task.Base.Pollers.CreateDefault',
                properties: {},
                options: {
                    nodeId: null,
                    pollers: [
                        {
                            "type": "ipmi",
                            "pollInterval": 60000,
                            "config": {
                                "command": "sdr"
                            }
                        },
                        {
                            "type": "ipmi",
                            "pollInterval": 60000,
                            "config": {
                                "command": "selInformation"
                            }
                        },
                        {
                            "type": "ipmi",
                            "pollInterval": 60000,
                            "config": {
                                "command": "sel"
                            }
                        },
                        {
                            "type": "ipmi",
                            "pollInterval": 60000,
                            "config": {
                                "command": "selEntries"
                            }
                        },
                        {
                            "type": "ipmi",
                            "pollInterval": 15000,
                            "config": {
                                "command": "chassis"
                            }
                        },
                        {
                            "type": "ipmi",
                            "pollInterval": 30000,
                            "config": {
                                "command": "driveHealth"
                            }
                        }
                    ]
                }
            },
            waitOn: {
                'discovery-refresh-graph': 'succeeded'
            }
        }
    ]
};
