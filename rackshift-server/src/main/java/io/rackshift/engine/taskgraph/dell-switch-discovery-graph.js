// Copyright 2018, Dell EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Dell Switch Bmp Discovery",
    "injectableName": "Graph.Switch.Discovery.Dell.Bmp",
    "tasks": [
        {
            "label": "catalog-switch",
            "taskDefinition": {
                "friendlyName": "Catalog Dell Switch",
                "injectableName": "Task.Inline.Catalog.Switch.Dell",
                "implementsTask": "Task.Base.Linux.Commands",
                "options": {
		    "commands": [
                        {
                            "downloadUrl": "{{ api.templates }}/dell-switch-catalog.sh?nodeId={{ task.nodeId }}",
                            "catalog": { "format": "json", "source": "sysinfo" }
                        }
                    ]
                },
                "properties": {}
            }
        }
    ]
};
