// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
  "friendlyName": "Docker Container List",
  "injectableName": "Graph.Docker.ListContainers",
  "options": {
    "docker-list": {
      "exec": [
        {"method": "list", "args": [{"all": 1}],
          "emit": {"docker-reconciler": {"type": "containers", "ref": 0}}}
      ]
    }
  },
  "tasks": [
    {
      "label": "docker-list",
      "taskName": 'Task.Docker',
    }
  ]
};
