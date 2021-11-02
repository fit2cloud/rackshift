// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    "friendlyName": "Docker Service",
    "injectableName": "Graph.Service.Docker",
    "serviceGraph": true,
    "options": {
        "docker-reconciler": {
            schedulerOverrides: {
                timeout: -1
            }
        }
    },
    "tasks": [
        {
            "label": "docker-reconciler",
            "taskName": "Task.Docker.Reconciler"
        }
    ]
};
