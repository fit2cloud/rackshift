// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Flash Ami BIOS',
    injectableName: 'Task.Linux.Flash.Ami.Bios',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'flash-ami.json',
    options: {
        downloadDir: '/opt/downloads',
        backupFile: '{{ context.ami.systemRomId }}.bin',
        commands: [
            // Backup firmware
            'cd /opt/ami; ' +
                'sudo ./afulnx_64 {{ options.backupFile }} /O',
            'sudo curl -T /opt/ami/{{ options.backupFile }} ' +
                '{{ api.files }}/{{ task.nodeId }}-{{ options.backupFile }}',
            'sudo mv {{ options.downloadDir }}/{{ options.file }} /opt/ami/{{ options.file }}',
            // For some reason, when the command is run raw, we think
            // it finishes before it actually does. If we send output
            // to a file and then send that file back + parse it, we
            // should be okay.
            'cd /opt/ami;' + 'sudo ./afulnx_64 {{ options.file }}' +
                ' /P /B /K /N /ME > /tmp/renasar-afu-flash.log;' +
                'sleep 3;tail -n 100 /tmp/renasar-afu-flash.log'
        ]
    },
    properties: {
        flash: {
            type: 'bios',
            vendor: 'ami'
        }
    },
    requiredProperties: {
        context: {
            ami: 'systemRomId'
        }
    }
};
