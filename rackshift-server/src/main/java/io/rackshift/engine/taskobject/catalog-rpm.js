// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'catalog rpm',
    injectableName: 'Task.Catalog.Rpm',
    implementsTask: 'Task.Base.Ssh.Command',
    options: {
        commands: {
            catalog: {
                source: 'rpm',
                format: 'json'
            },
            command:"sudo echo '['; rpm -qa --queryformat '\\{\"name\":\"%{NAME}\","+
                "\"version\":\"%{VERSION}\",\"release\":\"%{RELEASE}\","+
                "\"summary\":\"%{SUMMARY}\" \\},' | sed '$s/.$//'; echo ']'"
        }
    },
    properties: {}
};
