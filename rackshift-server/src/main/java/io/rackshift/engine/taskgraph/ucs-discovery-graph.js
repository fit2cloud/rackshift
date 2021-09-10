// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Ucs Discovery',
    injectableName: 'Graph.Ucs.Discovery',
    options: {
        defaults: {
            uri: null
        },
        'when-discover-physical-ucs' : {
            discoverPhysicalServers: 'true',
            updateExistingCatalog: true,
            when: '{{options.discoverPhysicalServers}}'
        },
        'when-discover-logical-ucs' : {
            discoverLogicalServer: 'true',
            updateExistingCatalog: true,
            when: '{{options.discoverLogicalServer}}'
        },
        'when-catalog-ucs' : {
            autoCatalogUcs: 'true',
            updateExistingCatalog: true,
            when: '{{options.autoCatalogUcs}}'
        },
        'skip-pollers': {
            skipPollersCreation: 'false',
            when: '{{options.skipPollersCreation}}'
        },
        'skip-ipmi-pollers': {
            skipIPMIPollersCreation: 'true',
            when: '{{options.skipIPMIPollersCreation}}'
        }
    },
    tasks: [
        {
            'x-description': 'Check to see if we need to discover physical UCS servers',
            label: 'when-discover-physical-ucs',
            taskName: 'Task.Evaluate.Condition',
            ignoreFailure: true
        },
        {
            'x-description': 'Check to see if we need to discover logical UCS servers',
            label: 'when-discover-logical-ucs',
            taskName: 'Task.Evaluate.Condition',
            ignoreFailure: true,
        },
        {
            'x-description': 'Discover physical UCS servers',
            label: 'ucs-physical-discovery',
            taskName: 'Task.Ucs.Discovery',
            waitOn: {
                'when-discover-physical-ucs': 'succeeded'
            }
        },
        {
            'x-description': 'Discover logical UCS servers',
            label: 'ucs-logical-discovery',
            taskName: 'Task.Ucs.Service.Profile.Discovery',
            waitOn: {
                'when-discover-logical-ucs': 'succeeded'
            }
        },
        {
            'x-description': 'UCS physical discovery finished',
            label: 'ucs-physical-discovery-done',
            taskName: 'Task.noop',
            waitOn: {
                anyOf: {
                    'when-discover-physical-ucs': 'failed',
                    'ucs-physical-discovery': 'succeeded'
                }
            }
        },
        {
            'x-description': 'UCS logical discovery finished',
            label: 'ucs-logical-discovery-done',
            taskName: 'Task.noop',
            waitOn: {
                anyOf: {
                    'when-discover-logical-ucs': 'failed',
                    'ucs-logical-discovery': 'succeeded'
                }
            }
        },
        {
            'x-description': 'Check to see if cataloging should be done',
            label: 'when-catalog-ucs',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'ucs-physical-discovery-done': 'succeeded',
                'ucs-logical-discovery-done': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'ucs-catalog',
            taskName: 'Task.Ucs.Catalog',
            waitOn: {
                'when-catalog-ucs': 'succeeded'
            }
        },
        {
            label: 'skip-pollers',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'ucs-catalog': 'finished'
            },
            ignoreFailure: true
        },
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
            },
            waitOn: {
                'ucs-catalog': 'succeeded',
                'skip-pollers': 'failed'
            }
        },
        {
            label: 'skip-ipmi-pollers',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'ucs-catalog': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'create-ucs-ipmi-pollers',
            taskDefinition: {
                friendlyName: 'Create Default IPMI Pollers',
                injectableName: 'Task.Inline.Pollers.CreateDefault',
                implementsTask: 'Task.Base.Pollers.CreateDefault',
                properties: {},
                options: {
                    nodeId: null,
                    pollers: [
                        {
                            'type': 'ipmi',
                            'pollInterval': 60000,
                            'config': {
                                'command': 'sdr'
                            }
                        },
                        {
                            'type': 'ipmi',
                            'pollInterval': 60000,
                            'config': {
                                'command': 'selInformation'
                            }
                        },
                        {
                            'type': 'ipmi',
                            'pollInterval': 60000,
                            'config': {
                                'command': 'sel'
                            }
                        },
                        {
                            'type': 'ipmi',
                            'pollInterval': 60000,
                            'config': {
                                'command': 'selEntries'
                            }
                        },
                        {
                            'type': 'ipmi',
                            'pollInterval': 60000,
                            'config': {
                                'command': 'chassis'
                            }
                        },
                        {
                            'type': 'ipmi',
                            'pollInterval': 60000,
                            'config': {
                                'command': 'driveHealth'
                            }
                        }
                    ]
                }
            },
            waitOn: {
                'ucs-catalog': 'succeeded',
                'skip-ipmi-pollers': 'failed'
            }
        },
        {
            'x-description': 'Set the final graph state to success when cataloging is skipped',
            label: 'noop',
            taskName: 'Task.noop',
            waitOn: {
                'when-catalog-ucs': 'failed'
            }
        }
    ]
};
