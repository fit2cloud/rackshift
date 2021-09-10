// Copyright 2017, Dell EMC, Inc.
'use strict';

module.exports = {
    friendlyName: 'Update Switch Firmware',
    injectableName: 'Graph.Update.Switch.Firmware',
    options: {
        defaults: {
            endpoint: {
                ipaddress: null,
                username: null,
                password: null,
                switchType: null
            },
            switchModel: null,
            firmwareImages: null
        },
        'Get-switch-config': {
            loginToken : 'Bearer {{ context.outputs["On-network-login"].restData.body.token }}'
        },
        'Post-update-switch-firmware': {
            loginToken : 'Bearer {{ context.outputs["On-network-login"].restData.body.token }}'
        },

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
        },
        {
            label: 'Post-update-switch-firmware',
            taskName: 'Task.Post.Update.Switch' ,
            waitOn: {
                "On-network-login": "succeeded"
            }
        }

    ]
};