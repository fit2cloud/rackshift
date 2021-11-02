// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Node sleep',
    injectableName: 'Task.Node.Sleep',
    implementsTask: 'Task.Base.Linux.Commands',
    options: {
        duration: 10, //in seconds
        commands: ['sudo sleep {{options.duration}}']
    },
    properties: {}
};
