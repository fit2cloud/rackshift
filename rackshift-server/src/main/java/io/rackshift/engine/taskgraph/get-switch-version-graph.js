// Copyright 2017, Dell EMC, Inc.
'use strict';

module.exports = {
    friendlyName: 'Get Switch Version Graph',
    injectableName: 'Graph.Get.Switch.Version',
    options: {
        defaults: {
            endpoint: {
                ipaddress: null,
                username: null,
                password: null,
                switchType: null
            }
        },
        'Get-switch-version': {
            loginToken : 'Bearer {{ context.outputs["On-network-login"].restData.body.token }}'
        }

    },
    tasks: [
        {
            label: 'On-network-login',
            taskName: 'Task.Post.Login.On-network'
        },
        {
            label: 'Get-switch-version',
            taskName: 'Task.Get.Switch.Version' ,
            waitOn: {
                'On-network-login': 'succeeded'
            }
        }
    ]
};
