// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Send Finish Trigger',
    injectableName: 'Task.Trigger.Send.Finish',
    implementsTask: 'Task.Base.Trigger',
    options: {
        triggerMode: 'send',
        triggerType: 'finish',
        triggerGroup: 'default'
    },
    properties: {}
};
