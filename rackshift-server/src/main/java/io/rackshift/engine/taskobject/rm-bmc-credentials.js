// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Remove BMC Credentials',
    injectableName: 'Task.Remove.BMC.Credentials',
    implementsTask: 'Task.Base.Linux.Commands',
    options: {
        users: [],
        commands: [
            {
                command: 'sudo ./remove_bmc_credentials.sh',
                downloadUrl: '{{ api.templates }}/remove_bmc_credentials.sh?nodeId={{ task.nodeId }}'
            }
        ]
    },
    properties: {
        os: {
            linux: {
                type: 'microkernel'
            }
        }

    }
};
