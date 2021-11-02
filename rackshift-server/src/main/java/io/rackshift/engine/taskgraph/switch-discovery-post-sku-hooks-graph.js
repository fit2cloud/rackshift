// Copyright 2016, EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Switch Discovery SKU Graph post-hooks",
    "injectableName": "Graph.Switch.SKU.Discovery.Hooks.Post",
    "tasks": [
        {
            "label": "exit-switch-taskrunner-success",
            "taskDefinition": {
                "friendlyName": "Exit switch taskrunner with success",
                "injectableName": "Task.Inline.ExitSwitchTaskRunner.Succeeded",
                "implementsTask": "Task.Base.ShellReboot",
                "options": {
                    "rebootCode": 0
                },
                "properties": {}
            }
        },
        {
            "label": "node-discovered-alert",
            "taskName": "Task.Alert.Node.Discovered",
            "waitOn": {
                "exit-switch-taskrunner-success": "succeeded"
            }
        }
    ]
};
