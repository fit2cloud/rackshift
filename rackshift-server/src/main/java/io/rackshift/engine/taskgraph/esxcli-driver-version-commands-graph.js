//Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.

'use strict';

module.exports = {
    friendlyName: 'ESXi Driver Version Retrieval',
    injectableName: 'Graph.ESXi.Driver.Version.Retrieval',
    options: {
        "versionRetrieval": {
            "updateExistingCatalog": true
        },
        defaults: {
            commands: null
            }
    },
    tasks: [
        {
            label: 'versionRetrieval',
            taskDefinition: {
                injectableName: "Task.Run.Ssh",
                friendlyName: "Ssh and run Esxi commands",
                implementsTask: 'Task.Base.Ssh.Catalog',
                options: {
                    commands: null
                },
                properties: {}
            }
        }
    ]
};
