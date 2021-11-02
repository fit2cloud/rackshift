// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'SKU Discovery',
    injectableName: 'Graph.SKU.Discovery',
    options: {
        defaults: {
            graphOptions: {
                target: null
            },
            nodeId: null,
            nodeIds: [ "{{ options.nodeId }}" ]
        },
        'skip-pollers' : {
            skipPollersCreation: 'false',
            when: '{{options.skipPollersCreation}}'
        },
        'obm-option' : {
            autoCreateObm: 'false',
            when: '{{options.autoCreateObm}}'
        }
    },
    tasks: [
        {
            label: 'discovery-graph',
            taskDefinition: {
                friendlyName: 'Run Discovery Graph',
                injectableName: 'Task.Graph.Run.Discovery',
                implementsTask: 'Task.Base.Graph.Run',
                options: {
                    graphName: 'Graph.rancherDiscovery',
                    graphOptions: {}
                },
                properties: {}
            }
        },
        {
            label: 'obm-option',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
               'discovery-graph': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'noop-1',
            taskName: 'Task.noop',
            waitOn: {
                'obm-option': 'failed'
            }
        },
        {
            label: 'generate-sku',
            waitOn: {
                'discovery-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateSku'
        },
        {
            label: 'generate-tag',
            waitOn: {
                'discovery-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateTag'
        },
        {
            label: 'generate-enclosure',
            waitOn: {
                'discovery-graph': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateEnclosure',
            ignoreFailure: true
        },
        {
            label: 'skip-pollers',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'generate-enclosure': 'finished'
            },
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
                'discovery-graph': 'succeeded',
                'skip-pollers': 'failed'
            }
        },
        {
            label: 'noop-2',
            taskName: 'Task.noop',
            waitOn: {
                'skip-pollers': 'succeeded'
            }
        },
        {
            label: 'run-sku-graph',
            taskDefinition: {
                friendlyName: 'Run SKU-specific graph',
                injectableName: 'Task.Graph.Run.SkuSpecific',
                implementsTask: 'Task.Base.Graph.RunSku',
                options: {},
                properties: {}
            },
            waitOn: {
                'generate-sku': 'succeeded'
            }

        }
    ]
};
