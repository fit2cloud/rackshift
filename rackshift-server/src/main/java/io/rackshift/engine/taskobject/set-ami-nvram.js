// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Set AMI NVRAM variables',
    injectableName: 'Task.Linux.SetNvram.Ami',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'firmware-update.json',
    options: {
        downloadDir: '/opt/downloads',
        commands: [
            // Backup original NVRAM settings
            'sudo /opt/ami/SCELNX_64 /o /s biosNvramSettingsBackup',
            'sudo curl -T ./biosNvramSettingsBackup ' +
                '{{ api.files }}/{{ task.nodeId }}-biosNvramSettingsBackup',
            // Set NVRAM settings
            'sudo /opt/ami/SCELNX_64 /i /s {{ options.downloadDir }}/{{ options.file }}',
            // Backup new NVRAM settings
            'sudo /opt/ami/SCELNX_64 /o /s biosNvramSettingsCurrent',
            'sudo curl -T ./biosNvramSettingsCurrent ' +
                '{{ api.files }}/{{ task.nodeId }}-biosNvramSettingsCurrent'
        ]
    },
    properties: {
        nvram: {
            vendor: 'ami'
        }
    }
};
