// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Completion URI Trigger',
    injectableName: 'Task.Wait.Completion.Uri',
    implementsTask: 'Task.Base.Wait.Completion.Uri',
    options: {
        completionUri: 'renasar-ansible.pub'
    },
    properties: {}
};
