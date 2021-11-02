'use strict';

module.exports = {
    friendlyName: "Add Volume",
    injectableName: "Task.Add.Volume",
    implementsTask: "Task.Base.Add.Volume",
    options: {
        username: null,
        password: null,
        drives: null,
        raidLevel: null,
        name: null,
        sizeInBytes: null,
        ipAddress: null
    },
    properties: {}
};
