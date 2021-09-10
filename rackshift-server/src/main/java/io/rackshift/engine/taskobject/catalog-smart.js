// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog S.M.A.R.T',
    injectableName: 'Task.Catalog.smart',
    implementsTask: 'Task.Base.Linux.Catalog',
    options: {
        commands: [
            {
                command: 'sudo bash get_smart.sh',
                downloadUrl: '{{ api.templates }}/get_smart.sh?nodeId={{ task.nodeId }}'
            }
        ]
    },
    properties: {
        catalog: {
            type: 'smart'
        }
    }
};
