// Copyright 2017, Dell EMC, Inc.
'use strict';

module.exports = {
    friendlyName: 'Get Switch Config Graph',
    injectableName: 'Graph.Get.Switch.Config',
    options: {
        defaults: {
            endpoint: {
                ipaddress: null,
                username: null,
                password: null,
                switchType: null
            }
        },
        'Get-switch-config': {
            loginToken : 'Bearer {{ context.outputs["On-network-login"].restData.body.token }}'
        }
    },
    tasks: [
        {
            label: 'On-network-login',
            taskName: 'Task.Post.Login.On-network'
        },
        {
            label: 'Get-switch-config',
            taskName: 'Task.Get.Switch.Config',
            waitOn: {
                'On-network-login': 'succeeded'
            }
        }
    ]
};
