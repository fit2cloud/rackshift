// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Download Files to node',
    injectableName: 'Task.Linux.DownloadFiles',
    implementsTask: 'Task.Base.Linux.Commands',
    optionsSchema: 'download-file.json',
    options: {
        downloadDir: '/opt/downloads',
        fileMd5Uri: '{{ api.files }}/{{ options.file }}/md5',
        fileUri: '{{ api.files }}/{{ options.file }}',
        outputPath: '{{ options.downloadDir }}/{{ options.file }}',
        commands: [
            'curl --retry 3 {{ options.fileUri }} -o {{ options.outputPath }}',
            // Instead of adding long and obtuse sed commands to remove
            // the quotes from curl output, use \\" to force `test` to compare
            // both strings as being wrapped in quotes.
            //
            // The test command will exit 1 if the strings do not match.
            'test `curl {{ options.fileMd5Uri }}` = \\"`md5sum {{ options.outputPath }}' +
                '| awk \'{print $1}\'`\\"'
        ]
    },
    properties: {}
};
