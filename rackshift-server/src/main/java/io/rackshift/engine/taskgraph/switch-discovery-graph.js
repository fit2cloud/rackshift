// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Switch Discovery',
    injectableName: 'Graph.Switch.Discovery',
    options: {
        defaults: {
            nodeId: null,
            nodeIds: [ "{{ options.nodeId }}" ]
        },
        'create-switch-snmp-pollers': {
            pollers: [
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-interface-bandwidth-utilization"
                    }
                },
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-interface-state"
                    }
                },
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-processor-load"
                    }
                },
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-memory-usage"
                    }
                },
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-txrx-counters"
                    }
                },
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-switch-sensor-status"
                    }
                }
            ]
        }
    },
    tasks: [
        {
            label: 'ping-host',
            taskName: 'Task.Snmp.Ping'
        },
        {
            label: 'collect-snmp',
            taskName: 'Task.Snmp.Collect.Discovery',
            waitOn: {
                'ping-host' : 'succeeded'
            }
        },
        {
            label: 'catalog-snmp',
            taskName: 'Task.Snmp.Catalog',
            waitOn: {
                'ping-host': 'succeeded'
            }
        },
        {
            label: 'update-lookups',
            taskName: 'Task.Snmp.Update.Lookups',
            waitOn: {
                'catalog-snmp': 'succeeded'
            }
        },
        {
            label: 'create-switch-snmp-pollers',
            taskName: 'Task.Pollers.CreateDefault',
            waitOn: {
                'catalog-snmp': 'succeeded'
            }
        },
        {
            label: 'update-node-name',
            taskName: 'Task.Snmp.Node.Update',
            waitOn: {
                'catalog-snmp': 'succeeded'
            }
        },
        {
            label: 'switch-node-relations',
            taskDefinition: {
                friendlyName: 'Run switch to nodes relations',
                injectableName: 'Task.Catalog.SwitchRelations',
                implementsTask: 'Task.Base.Catalog.SwitchRelations',
                options: {},
                properties: {}
            },
            waitOn: {
                'catalog-snmp': 'succeeded'
            }
        },
        {
            label: 'generate-sku',
            waitOn: {
                'switch-node-relations': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateSku'
        },
        {
            label: 'generate-tag',
            waitOn: {
                'switch-node-relations': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateTag'
        },
        {
            label: 'node-discovered-alert',
            taskName: 'Task.Alert.Node.Discovered',
            waitOn: {
                'update-node-name': 'finished'
            }
        }
    ]
};
