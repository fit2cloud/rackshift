// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Boot LiveCD',
    injectableName: 'Task.Os.Boot.LiveCD',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'boot-livecd.json',
    options: {
        profile: 'boot-livecd.ipxe',
        version: 'livecd',
        repo: '{{file.server}}/LiveCD/{{options.version}}'
    },
    properties: {
        os: {
            linux: {
                distribution: 'livecd'
            }
        }
    }
};
