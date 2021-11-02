// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Bootstrap And Remove BMC Credentials',
    injectableName: 'Graph.Bootstrap.With.BMC.Credentials.Remove',
    options: {
        defaults: {
            graphOptions: {
                target: null
            },
            nodeId: null,
        },

    },
    tasks: [
        {
            label: 'boot-graph',
            taskDefinition: {
                friendlyName: 'Boot Graph',
                injectableName: 'Task.Graph.Run.Boot',
                implementsTask: 'Task.Base.Graph.Run',
                options: {
                    graphName: 'Graph.BootstrapUbuntu',
                    defaults : {
                        graphOptions: {   }
                    }
                },
                properties: {}
            }
        },
        {
            label: 'remove-bmc-credentials-graph',
            taskDefinition: {
                friendlyName: 'Run BMC Rmove Credential Graph',
                injectableName: 'Task.Graph.Run.Bmc',
                implementsTask: 'Task.Base.Graph.Run',
                options: {
                    graphName: 'Graph.Remove.Bmc.Credentials',
                    defaults : {
                        graphOptions: {   }
                    }
                },
                properties: {}
            },
            waitOn: {
                'boot-graph': 'finished'
            }
        },
        {
            label: 'finish-bootstrap-trigger',
            taskName: 'Task.Trigger.Send.Finish',
            waitOn: {
                'remove-bmc-credentials-graph': 'finished'
            }
        }

    ]
};
