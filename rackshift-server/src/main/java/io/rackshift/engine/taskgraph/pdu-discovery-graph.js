// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'PDU Discovery',
    injectableName: 'Graph.PDU.Discovery',
    options: {
        defaults: {
            nodeId: null,
            nodeIds: [ "{{ options.nodeId }}" ]
        },
        'create-pdu-snmp-pollers': {
            pollers: [
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-pdu-power-status"
                    }
                },
                {
                    "type": "snmp",
                    "pollInterval": 60000,
                    "config": {
                        "metric": "snmp-pdu-sensor-status"
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
            label: 'create-pdu-snmp-pollers',
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
            label: 'pdu-node-relations',
            taskDefinition: {
                friendlyName: 'Run pdu to nodes relations',
                injectableName: 'Task.Catalog.PduRelations',
                implementsTask: 'Task.Base.Catalog.PduRelations',
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
                'pdu-node-relations': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateSku'
        },
        {
            label: 'generate-tag',
            waitOn: {
                'pdu-node-relations': 'succeeded'
            },
            taskName: 'Task.Catalog.GenerateTag'
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
