// Copyright 2018, Dell EMC, Inc.

"use strict";

module.exports = {
    "friendlyName": "Dell Switch Nos installation",
    "injectableName": "Graph.Switch.Dell.Nos.Install",
    "tasks": [
        {
            "label": "install-os",
            "taskDefinition": {
                "friendlyName": "Dell Switch Nos install",
                "injectableName": "Task.Inline.Switch.Dell.Nos.Install",
                "implementsTask": "Task.Base.Linux.Commands",
                "options": {
                    "nosImageUri": "{{ file.server }}/PKGS_OS10-Enterprise-10.3.1E.121-installer-x86_64.bin",
                    "commands": [
                        {
                            "downloadUrl": "{{ api.templates }}/dell-switch-nos-install.sh?nodeId={{ task.nodeId }}",
                        }
                    ]
                },
                "properties": {}
            }
        }
    ]
};
