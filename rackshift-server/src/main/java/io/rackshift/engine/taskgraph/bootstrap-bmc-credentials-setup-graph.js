// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Bootstrap And Set Credentials',
    injectableName: 'Graph.Bootstrap.With.BMC.Credentials.Setup',
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
            label: 'set-bmc-credentials-graph',
            taskDefinition: {
                friendlyName: 'Run BMC Credential Graph',
                injectableName: 'Task.Graph.Run.Bmc',
                implementsTask: 'Task.Base.Graph.Run',
                options: {
                    graphName: 'Graph.Set.Bmc.Credentials',
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
                'set-bmc-credentials-graph': 'finished'
            }
        }

    ]
};
