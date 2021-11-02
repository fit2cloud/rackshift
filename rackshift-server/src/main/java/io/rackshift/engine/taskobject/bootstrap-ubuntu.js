// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Bootstrap Ubuntu',
    injectableName: 'Task.Linux.Bootstrap.Ubuntu',
    implementsTask: 'Task.Base.Linux.Bootstrap',
    options: {
        kernelFile: 'vmlinuz-3.16.0-25-generic',
        initrdFile: 'initrd.img-3.16.0-25-generic',
        basefsFile: 'base.trusty.3.16.0-25-generic.squashfs.img',
        overlayfsFile: 'discovery.overlay.cpio.gz',
        kernelUri: '{{ file.server }}/common/{{ options.kernelFile }}',
        initrdUri: '{{ file.server }}/common/{{ options.initrdFile }}',
        basefsUri: '{{ file.server }}/common/{{ options.basefsFile }}',
        overlayfsUri: '{{ file.server }}/common/{{ options.overlayfsFile }}',
        profile: 'linux.ipxe',
        comport: 'ttyS0'
    },
    properties: {
        os: {
            linux: {
                distribution: 'ubuntu',
                release: 'trusty',
                kernel: '3.16.0-25-generic'
            }
        }
    }
};
