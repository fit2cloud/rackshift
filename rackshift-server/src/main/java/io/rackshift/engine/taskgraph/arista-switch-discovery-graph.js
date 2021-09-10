// Copyright 2016, EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Arista Switch ZTP Discovery",
    "injectableName": "Graph.Switch.Discovery.Arista.Ztp",
    "tasks": [
        {
            "label": "catalog-switch",
            "taskDefinition": {
                "friendlyName": "Catalog Arista Switch",
                "injectableName": "Task.Inline.Catalog.Switch.Arista",
                "implementsTask": "Task.Base.Linux.Commands",
                "options": {
		    "commands": [
                        {
                            "downloadUrl": "{{ api.templates }}/arista-catalog-version.py?nodeId={{ task.nodeId }}",
                            "catalog": { "format": "json", "source": "version" }
                        }
                    ]
                },
                "properties": {}
            }
        }
    ]
};
