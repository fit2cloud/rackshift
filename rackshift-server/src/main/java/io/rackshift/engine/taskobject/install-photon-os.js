// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install Photon OS',
    injectableName: 'Task.Os.Install.PhotonOS',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'install-photon-os.json',
    options: {
        osType: 'linux', //readonly options, should avoid change it
        profile: 'install-photon-os.ipxe',
        installScript: 'photon-os-ks',
        installScriptUri: '{{ api.templates }}/{{ options.installScript }}?nodeId={{ task.nodeId }}', //jshint ignore: line
        hostname: 'localhost',
        comport: 'ttyS0',
        rackhdCallbackScript: 'photon-os.rackhdcallback',
        version: "1.0",
        repo: '{{file.server}}/photon/{{options.version}}',
        rootPassword: "RackHDRocks!",
        installDisk: "/dev/sda",
        installType: "minimal",

        //Some milestones are injected where can add custom commands.
        //Refer to below link for those injectable points:
        //https://github.com/vmware/photon/blob/master/docs/kickstart.md
        progressMilestones: {
            //jshint ignore: start
            requestProfile:     { value: 1, description: 'Enter ipxe and request OS installation profile' },
            enterProfile:       { value: 2, description: 'Enter profile, start to download installer'},
            startInstaller:     { value: 3, description: 'Start installer and prepare installation' },
            postConfig:         { value: 4, description: 'Enter post OS configuration'},
            completed:          { value: 5, description: 'Finished OS installation'}
            //jshint ignore: end
        }
    },
    properties: {
        os: {
            linux: {
                distribution: 'photon-os'
            }
        }
    }
};
