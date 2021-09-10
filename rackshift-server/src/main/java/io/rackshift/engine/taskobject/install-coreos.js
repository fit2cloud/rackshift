// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install CoreOS',
    injectableName: 'Task.Os.Install.CoreOS',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'install-coreos.json',
    options: {
        osType: 'linux',
        profile: 'install-coreos.ipxe',
        installScript: 'install-coreos.sh',
        installScriptUri: '{{ api.templates }}/{{ options.installScript }}?nodeId={{ task.nodeId }}', //jshint ignore: line
        comport: 'ttyS0',
        hostname: 'coreos-node',
        installDisk: '/dev/sda',
        repo: '{{file.server}}/coreos',
        version: 'current',
        rootPassword: "RackHDRocks!",

        //Some milestones are injected where can add custom commands.
        //Refer to below links for those injectable points:
        // - https://coreos.com/os/docs/latest/booting-with-ipxe.html
        // - https://coreos.com/os/docs/latest/installing-to-disk.html
        progressMilestones: {
            //jshint ignore: start
            requestProfile: { value: 1, description: 'Enter ipxe and request OS installation profile' },
            enterProfile:   { value: 2, description: 'Enter profile, start to download installer' },
            startInstaller: { value: 3, description: 'Boot into installer' },
            installToDisk:  { value: 4, description: 'Install image to disk' },
            completed:      { value: 5, description: 'Finished OS installation' }
            //jshint ignore: end
        }
    },
    properties: {
        os: {
            linux: {
                distribution: 'coreos'
            }
        }
    }
};
