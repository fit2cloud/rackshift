//Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
		friendlyName: 'dell wsman download catalog file',
		injectableName: 'Task.Dell.Wsman.Download.Catalog',
		implementsTask: 'Task.Base.Dell.Wsman.Download',
		options: {
		   	action: 'download',
			fileName: null,
			fileURL: null,
			targetLocation: null
		},
		properties: {}
};

