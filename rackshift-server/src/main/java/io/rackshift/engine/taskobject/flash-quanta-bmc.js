// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Flash Quanta BMC',
    injectableName: 'Task.Linux.Flash.Quanta.Bmc',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'firmware-update.json',
    options: {
        downloadDir: '/opt/downloads',
        commands: [
            // Backup files
            'sudo /opt/socflash/socflash_x64 -b /opt/uploads/bmc-backup.bin',
            'sudo curl -T /opt/uploads/bmc-backup.bin ' +
                '{{ api.files }}/{{ task.nodeId }}-bmc-backup.bin',
            // Flash files
            'sudo /opt/socflash/socflash_x64 -s option=x ' +
                'flashtype=2 if={{ options.downloadDir }}/{{ options.file }}',
            // Wait for the new BMC to take effect, suggested in script provided by Quanta
            // Otherwise following bmc related tasks (if any) might possibly fail
            'sleep 90'
        ]
    },
    properties: {
        flash: {
            type: 'bmc',
            vendor: {
                quanta: { }
            }
        }
    }
};
