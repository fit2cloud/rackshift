// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Redfish Discovery',
    injectableName: 'Graph.Redfish.Discovery',
    options: {
        defaults: {
            uri: null
        },
        'when-catalog-emc' : {
            autoCatalogEmc: 'false',
            when: '{{options.autoCatalogEmc}}'
        },
        'when-pollers-emc' : {
            autoCreatePollerEmc: 'false',
            when: '{{options.autoCreatePollerEmc}}'
        }
    },
    tasks: [
        {
            'x-description': 'Enumerate the redfish endpoint',
            label: 'redfish-client-discovery',
            taskName: 'Task.Redfish.Discovery'
        },
        {
            label: 'create-redfish-pollers',
            taskDefinition: {
                friendlyName: 'Create Default Pollers',
                injectableName: 'Task.Inline.Pollers.Redfish.CreateDefault',
                implementsTask: 'Task.Base.Pollers.Redfish.CreateDefault',
                properties: {},
                options: {
                    pollers: [
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "thermal"
                            }
                        },
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "power"
                            }
                        },
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "managers.logservices"
                            }
                        },    
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "systems.logservices"
                            }
                        }
                    ]
                }
            },
            waitOn: {
                'redfish-client-discovery': 'succeeded'
            }
        },
        {
            'x-description': 'Indicate to downstream tasks if cataloging should be done',
            label: 'when-catalog-emc',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'create-redfish-pollers': 'succeeded'
            }
        },
        {
            'x-description': 'Perform cataloging of the EMC endpoints placed into the graph context',
            label: 'emc-redfish-catalog',
            taskName: 'Task.Emc.Redfish.Catalog',
            waitOn: {
                'when-catalog-emc': 'succeeded'
            },
            ignoreFailure: true
        },    
        {
            'x-description': 'Perform cataloging of the Dell endpoints placed into the graph context',
            label: 'general-redfish-catalog',
            taskName: 'Task.General.Redfish.Catalog',
            waitOn: {
                'redfish-client-discovery': 'succeeded'
            },
            ignoreFailure: true
       },
       {
            'x-description': 'Indicate to downstream tasks if EMC pollers should be created',
            label: 'when-pollers-emc',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'create-redfish-pollers': 'succeeded'
            }
        },     
        {
            label: 'create-emc-redfish-pollers',
            taskDefinition: {
                friendlyName: 'Create Default Pollers',
                injectableName: 'Task.Inline.Pollers.Redfish.CreateDefault',
                implementsTask: 'Task.Base.Pollers.Redfish.CreateDefault',
                properties: {},
                options: {
                    pollers: [
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "fabricservice"

                            }
                        },
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "elements.thermal"

                            }
                        },
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "elements.power"

                            }
                        }
                    ]
                }
            },
            waitOn: {
                'when-pollers-emc': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            'x-description': 'Set the final graph state to success when cataloging is skipped',
            label: 'noop',
            taskName: 'Task.noop',
            waitOn: {
                'when-catalog-emc': 'failed',
                'when-pollers-emc': 'failed'
            }
        }
    ]
};
