// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
  "friendlyName": "Docker Stop",
  "injectableName": "Graph.Docker.Stop",
  "options": {
    "docker-stop": {
      "exec": [
        {"method": "stop", "args": ["{{options.containerId}}", {}]}
      ],
      "containerId": "$containerId"
    }
  },
  "tasks": [
    {
      "label": "docker-stop",
      "taskName": "Task.Docker"
    }
  ]
};
