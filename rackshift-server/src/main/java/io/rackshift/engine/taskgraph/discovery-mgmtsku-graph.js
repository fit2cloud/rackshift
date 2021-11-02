// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'MgmtSKU Discovery',
    injectableName: 'Graph.MgmtSKU.Discovery',
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
            label: 'discovery-mgmt-graph',
            taskDefinition: {
                friendlyName: 'Run Discovery Graph',
                injectableName: 'Task.Graph.Run.Discovery',
                implementsTask: 'Task.Base.Graph.Run',
                options: {
                    graphName: 'Graph.Mgmt.Discovery',
                    graphOptions: {}
                },
                properties: {}
            },
        },
        {
            label: 'generate-mgmt-sku',
            waitOn: {
                'discovery-mgmt-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateSku'
        },
        {
            label: 'generate-mgmt-tag',
            waitOn: {
                'discovery-mgmt-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateTag'
        },
        {
            label: 'generate-enclosure',
            waitOn: {
                'discovery-mgmt-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateEnclosure',
            ignoreFailure: true
        },
        {
            label: 'create-mgmt-default-pollers',
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
                'discovery-mgmt-graph': 'succeeded'
            }
        },
        {
            label: 'run-mgmtsku-graph',
            taskDefinition: {
                friendlyName: 'Run SKU-specific graph',
                injectableName: 'Task.Graph.Run.SkuSpecific',
                implementsTask: 'Task.Base.Graph.RunSku',
                options: {},
                properties: {}
            },
            waitOn: {
                'generate-mgmt-sku': 'succeeded'
            }
        }
    ]
};
