// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install CentOS',
    injectableName: 'Task.Os.Install.CentOS',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'install-centos.json',
    options: {
        osType: 'linux', //readonly options, should avoid change it
        profile: 'install-centos.ipxe',
        installScript: 'centos-ks',
        installScriptUri: '{{ api.templates }}/{{ options.installScript }}?nodeId={{ task.nodeId }}', //jshint ignore: line
        hostname: 'localhost',
        comport: 'ttyS0',
        rackhdCallbackScript: 'centos.rackhdcallback',
        repo: '{{file.server}}/centos/{{options.version}}/os/x86_64',
        rootPassword: "RackHDRocks!",
        remoteLogging: false,

        //jshint ignore: start
        //Some milestones are injected where can add custom commands.
        //Refer to below links for those injectable points:
        // - https://www.centos.org/docs/5/html/Installation_Guide-en-US/s1-kickstart2-preinstallconfig.html
        // - https://www.centos.org/docs/5/html/Installation_Guide-en-US/s1-kickstart2-postinstallconfig.html
        progressMilestones: {
            requestProfile: { value: 1, description: 'Enter ipxe and request OS installation profile' },
            enterProfile:   { value: 2, description: 'Enter profile, start to download installer'},
            startInstaller: { value: 3, description: 'Start installer and prepare installation' },
            preConfig:      { value: 4, description: 'Enter Pre OS configuration'},
            postConfig:     { value: 5, description: 'Enter Post OS configuration'},
            completed:      { value: 6, description: 'Finished OS installation'}
        }
        //jshint ignore: end
    },
    properties: {
        os: {
            linux: {
                distribution: 'centos'
            }
        }
    }
};
