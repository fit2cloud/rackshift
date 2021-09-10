// Copyright 2017, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Configure Megaraid Controller',
    injectableName: 'Task.Config.Megaraid',
    implementsTask: 'Task.Base.Linux.Commands',
    options: {
        hddArr: '{{ context.hddArr  }}',
        ssdStoragePoolArr:'{{ context.ssdStoragePoolArr  }}',
        ssdCacheCadeArr:'{{ context.ssdCacheCadeArr  }}',
        path: '{{ context.path  }}',
        controller: '{{ context.controller  }}',
        commands: [
            {
                command: 'sudo ./megaraid-config.sh',
                downloadUrl: '{{ api.templates }}/megaraid-config.sh?nodeId={{ task.nodeId }}'
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
