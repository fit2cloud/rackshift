// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Login on-network',
    injectableName: 'Task.Post.Login.On-network',
    implementsTask: 'Task.Base.Rest',
    options: {
        url: '{{ server.onNetwork.url }}/login',
        method: 'POST',
        headers: { 'Content-Type':'application/json'},
        recvTimeoutMs: 360000,
        data:{
            'username': '{{ server.onNetwork.username }}',
            'password': '{{ server.onNetwork.password }}'
        }
    },
    properties: {}
};
