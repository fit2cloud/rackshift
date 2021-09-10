// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'catalog dpkg',
    injectableName: 'Task.Catalog.Dpkg',
    implementsTask: 'Task.Base.Ssh.Command',
    options: {
        commands: [
            {
                catalog: {
                    source: 'dpkg',
                    format: 'json'
                },
                command: " sudo echo '['; dpkg-query -W -f='{\"package\":\"${binary:Package}\","+
                    "\"version\":\"${source:Version}\", \"status\":\"${db:Status-Abbrev}\","+
                    " \"summary\": \"${binary:Summary}\"},' | sed '$s/.$//'; echo ']';"
            }
        ]
    },
    properties: {}
};
