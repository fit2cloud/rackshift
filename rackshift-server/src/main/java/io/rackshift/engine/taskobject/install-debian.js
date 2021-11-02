// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install Debian/Ubuntu',
    injectableName: 'Task.Os.Install.Debian',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'install-debian.json',
    options: {
        osType: 'linux', //readonly options, should avoid change it
        osName: 'debian', //use "ubuntu" if you want to install ubuntu.
        profile: 'install-debian.ipxe',
        installScript: 'debian-preseed',
        installScriptUri: '{{ api.templates }}/{{ options.installScript }}?nodeId={{ task.nodeId }}', //jshint ignore: line
        rackhdCallbackScript: 'debian.rackhdcallback',
        hostname: 'localhost',
        comport: 'ttyS0',
        version: 'stretch',
        repo: '{{file.server}}/{{options.osName}}',
        baseUrl: 'dists/{{ options.version }}/main/installer-amd64/current/images/netboot/{{options.osName}}-installer/amd64',
        rootPassword: "RackHDRocks!",
        interface: "auto",
        installDisk: "/dev/sda",
        kvm: false,
        kargs:{},

        //Some milestones are injected where can add custom commands.
        //Refer to below link for those injectable points:
        //https://www.debian.org/releases/stable/amd64/apbs05.html.en
        progressMilestones: {
            //jshint ignore: start
            requestProfile:     { value: 1, description: 'Enter ipxe and request OS installation profile' },
            enterProfile:       { value: 2, description: 'Enter profile, start to download installer'},
            startInstaller:     { value: 3, description: 'Start installer and prepare installation' },
            preConfig:          { value: 4, description: 'Enter pre OS configuration'},
            startPartition:     { value: 5, description: 'Start partition'},
            postConfig:         { value: 6, description: 'Enter post OS configuration'},
            completed:          { value: 7, description: 'Finished OS installation'}
            //jshint ignore: end
        }
    },
    properties: {
        os: {
            linux: {
                distribution: 'debian'
            }
        }
    }
};
