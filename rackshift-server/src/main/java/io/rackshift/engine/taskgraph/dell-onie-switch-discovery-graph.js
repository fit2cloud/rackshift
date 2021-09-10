// Copyright 2018, Dell EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Dell Switch Onie Discovery",
    "injectableName": "Graph.Switch.Discovery.Dell.Onie",
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
                            "downloadUrl": "{{ api.templates }}/dell-switch-onie-catalog.sh?nodeId={{ task.nodeId }}",
                            "catalog": { "format": "json", "source": "onieSysinfo" }
                        }
                    ]
                },
                "properties": {}
            }
        }
    ]
};
