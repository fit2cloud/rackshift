// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Get Switch Version',
    injectableName: 'Task.Get.Switch.Version',
    implementsTask: 'Task.Base.Rest.Catalog',
    options: {
        endpoint: '{{options.endpoint}}',
        loginToken: '{{options.loginToken}}',
        url: '{{ server.onNetwork.url }}/switchVersion',
        method: 'POST',
        headers: {'Authorization':'{{options.loginToken}}', 'Content-Type':'application/json'},
        recvTimeoutMs: 360000,
        data:{
            'endpoint': '{{options.endpoint}}'
        },
        source: 'version'
    },
    properties: {
        catalog: {
            type: 'version'
        }
    }
};
