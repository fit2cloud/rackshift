// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Bootstrap Rancher',
    injectableName: 'Task.Linux.Bootstrap.Rancher',
    implementsTask: 'Task.Base.Linux.Bootstrap',
    options: {
        kernelFile: 'vmlinuz-1.2.0-rancher',
        initrdFile: 'initrd-1.2.0-rancher',
        dockerFile: 'discovery.docker.tar.xz',
        kernelUri: '{{ file.server }}/common/{{ options.kernelFile }}',
        initrdUri: '{{ file.server }}/common/{{ options.initrdFile }}',
        dockerUri: '{{ file.server }}/common/{{ options.dockerFile }}',
        profile: 'rancherOS.ipxe',
        comport: 'ttyS0'
    },
    properties: {
        os: {
            linux: {
                distribution: 'rancher',
                release: '1.2.0',
                Linux: '4.9.78'
            }
        }
    }
};
