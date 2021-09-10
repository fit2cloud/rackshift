// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Run EMC Diag',
    injectableName: 'Task.Os.Run.Emc.Diag',
    implementsTask: 'Task.Base.Os.Install',
    options: {
        kernelFile: 'vmlinuz-4.1.15-diag',
        initrdFile: 'ramfs.lzma',
        kernelUri: '{{ file.server }}/common/{{ options.kernelFile }}',
        initrdUri: '{{ file.server }}/common/{{ options.initrdFile }}',
        profile: 'linux-generic.ipxe',
        comport: 'ttyS0',
        bootargs: 'rw'
    },
    properties: {
        os: {
            linux: {
                distribution: 'busybox'
            }
        }
    }
};
