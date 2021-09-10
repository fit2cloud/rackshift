// Copyright (C) 2016 Brocade Communications Systems, Inc.
// Copyright © 2018 Dell Inc. or its subsidiaries. All Rights Reserved.

"use strict";

module.exports = {
    "friendlyName": "Brocade Switch Discovery",
    "injectableName": "Graph.Switch.Discovery.Brocade.Ztp",
    "tasks": [
        {
            "label": "catalog-brocade-switch",
            "taskDefinition": {
                "friendlyName": "Catalog Brocade Switch",
                "injectableName": "Task.Inline.Catalog.Switch.Brocade",
                "implementsTask": "Task.Base.Linux.Commands",
                "options": {
                    "commands": [
                        {
                            "downloadUrl": "{{ api.templates }}/brocade-catalog-version.py?nodeId={{ task.nodeId }}",
                            "catalog": { "format": "json", "source": "version" }
                        }
                    ]
                },
                "properties": {}
            }
        }
    ]
};
