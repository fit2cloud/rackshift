// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
  "friendlyName": "Docker Start",
  "injectableName": "Graph.Docker.Start",
  "options": {
    "docker-start": {
      "exec": [
        {"method": "start", "args": ["{{options.containerId}}", {}]}
      ],
      "containerId": "$containerId"
    }
  },
  "tasks": [
    {
      "label": "docker-start",
      "taskName": "Task.Docker"
    }
  ]
};
