// Copyright 2016, EMC, Inc.

'use strict';
/*jshint multistr: true */

module.exports = {
    friendlyName: 'Bootstrap and Decommission a node',
    injectableName: 'Graph.Bootstrap.Decommission.Node',
    options: {
        'shell-commands': {
            commands:[
                { 
                  command: "for disk in `lsblk | grep disk | awk '{print $1}'`;\
                  do sudo sgdisk --zap-all /dev/$disk ; done"
                }
            ]
        },
        'when-secure-erase' : {
            useSecureErase: 'false',
            when: '{{options.useSecureErase}}'
        },
        'remove-bmc-credentials': {
            users: null
        },
        'bootstrap-rancher': {
            dockerFile: 'secure.erase.docker.tar.xz'
        }
    },
    tasks: [
        {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot',
            ignoreFailure: true
        },
        {
            label: 'reboot-start',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'set-boot-pxe': 'finished'
            }
        },
        {
            label: 'bootstrap-rancher',
            taskName: 'Task.Linux.Bootstrap.Rancher',
            waitOn: {
                'reboot-start': 'succeeded'
            }
        },
        {
            label: 'catalog-megaraid',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'bootstrap-rancher': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-driveid',
            taskName: 'Task.Catalog.Drive.Id',
            waitOn: {
                'catalog-megaraid': 'finished'
            },
            ignoreFailure: true
        },
	{
	    label: 'remove-bmc-credentials',
	    taskName: 'Task.Remove.BMC.Credentials',
	    waitOn: {
		'catalog-driveid': 'finished'
	    },
	    ignoreFailure: true
	},
        {
	    label: 'when-secure-erase',
	    taskName: 'Task.Evaluate.Condition',
	    waitOn: {
	         'remove-bmc-credentials': 'finished'
	    }
	},
        {
            label: 'shell-commands',
            taskName: 'Task.Linux.Commands',
            waitOn: {
                'when-secure-erase': 'failed'
            }
        },
        {
            label: 'drive-secure-erase',
            taskName: 'Task.Drive.SecureErase',
            waitOn: {
                'when-secure-erase': 'succeeded'
            }
        },
        {
            label: 'post-catalog-megaraid',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'drive-secure-erase': 'finished',
		'when-secure-erase': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'post-catalog-driveid',
            taskName: 'Task.Catalog.Drive.Id',
            waitOn: {
                'drive-secure-erase': 'finished',
		'when-secure-erase': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'reboot-after-shell-commands',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'shell-commands': 'finished',
		'when-secure-erase': 'failed'
            },
	    ignoreFailure: true
        },
        {
            label: 'reboot-after-drive-secure-erase',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'drive-secure-erase': 'finished',
		'when-secure-erase': 'succeeded'
            },
	    ignoreFailure: true
        },
        {
            label: 'finish-after-shell-commands',
            taskName: 'Task.Trigger.Send.Finish',
            waitOn: {
                'shell-commands': 'finished',
		'when-secure-erase': 'failed'
            }
        },
        {
            label: 'finish-after-drive-secure-erase',
            taskName: 'Task.Trigger.Send.Finish',
            waitOn: {
                'drive-secure-erase': 'finished',
		'when-secure-erase': 'succeeded'
            }
        }

    ]
};
