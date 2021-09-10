// Copyright 2018, Dell EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Dell Switch configuration",
    "injectableName": "Graph.Switch.Dell.Configuration",
    "tasks": [
        {
            "label": "config-switch",
            "taskDefinition": {
                "friendlyName": "Dell Switch configuration",
                "injectableName": "Task.Inline.Switch.Dell.Configuration",
                "implementsTask": "Task.Base.Linux.Commands",
                "options": {
                    "mgmtPort": "1/1",
                    "username": "rackhd",
                    "userPassword": "RackHDRocks1!",
                    "adminPassword": "RackHDRocks1!",
                    "hostname": "rackhd",
                    "ipAddr": "dhcp",
                    "commands": [
                        {
                            "downloadUrl": "{{ api.templates }}/dell-switch-basic-config.exp?nodeId={{ task.nodeId }}",
                        }
                    ]
                },
                "properties": {}
            }
        }
    ]
};
