// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
  "friendlyName": "Docker Restart",
  "injectableName": "Graph.Docker.Restart",
  "options": {
    "docker-restart": {
      "exec": [
        {"method": "restart", "args": ["{{options.containerId}}", {}]}
      ],
      "containerId": "$containerId"
    }
  },
  "tasks": [
    {
      "label": "docker-restart",
      "taskName": "Task.Docker"
    }
  ]
};
