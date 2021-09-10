// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog Ip',
    injectableName: 'Task.Ssh.Catalog.Ip',
    implementsTask: 'Task.Base.Ssh.Catalog',
    options: {
        commands: [
            {
                "command": "sudo ip -d addr show; sudo ip -d link show"
            }
        ]
    },
    properties: {}
};
