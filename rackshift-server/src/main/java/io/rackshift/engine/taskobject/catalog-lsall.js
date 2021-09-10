// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Catalog lsall',
    injectableName: 'Task.Catalog.lsall',
    implementsTask: 'Task.Base.Linux.Catalog',
    options: {
        commands: [
            'sudo lspci -nn -vmm',
            'sudo lshw -json',
            'sudo lsblk -o KNAME,TYPE,ROTA; echo BREAK; sudo lsscsi --size'
        ]
    },
    properties: {
        catalog: {
            type: 'lsall'
        }
    }
};
