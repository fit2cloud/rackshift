// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Flash MegaRAID Controller',
    injectableName: 'Task.Linux.Flash.LSI.MegaRAID',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'flash-megaraid.json',
    options: {
        downloadDir: '/opt/downloads',
        adapter: '0',
        commands: [
            'sudo /opt/MegaRAID/storcli/storcli64 /c{{ options.adapter }} download ' +
                'file={{ options.downloadDir }}/{{ options.file }} noverchk',
            'sudo /opt/MegaRAID/MegaCli/MegaCli64 -AdpSetProp -BatWarnDsbl 1 ' +
                '-a{{ options.adapter }}',
        ]
    },
    properties: {
        flash: {
            type: 'storage',
            vendor: {
                lsi: {
                    controller: 'megaraid'
                }
            }
        }
    }
};
