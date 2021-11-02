// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Analyze OS Repository',
    injectableName: 'Task.Base.Os.Analyze.Repo',
    runJob: 'Job.Os.Analyze.Repo',
    optionsSchema: {
        properties: {
            version: {
                "description": "The version of target OS",
                "type": "string",
                "pattern": "^[-0-9a-zA-Z._*+#]+$"
            },
            repo: {
                "description": "The OS http repository for installation",
                "type": "string",
                "pattern": "^http://"
            },
            osName: {
                'enum': ['ESXi']
            }
        },
        required: ['osName', 'repo', 'version']
    },
    requiredProperties: {
    },
    properties: {
    }
};
