// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
  "friendlyName": "Docker Run",
  "injectableName": "Graph.Docker.Run",
  "options": {
    "docker-run": {
      "exec": [
        {"method": "pull", "args": ["{{options.image}}", {}], "then": [
          {"method": "run", "args": ["{{options.image}}", {}]}
        ]}
      ],
      "image": "ubuntu:latest"
    }
  },
  "tasks": [
    {
      "label": "docker-run",
      "taskName": "Task.Docker"
    }
  ]
};
