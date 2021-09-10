//Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
		friendlyName: 'dell wsman firmware collect ',
		injectableName: 'Task.Dell.Wsman.Firmware.Collect',
		implementsTask: 'Task.Base.Dell.Wsman.Firmware.Collect',
		options: {
		   	action: 'collectFirmware',
		   	catalogPath: null,
		   	type: null,
		   	updateableComponents: null
		},
		properties: {}
};

