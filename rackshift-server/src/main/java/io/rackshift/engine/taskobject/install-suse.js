// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install SUSE',
    injectableName: 'Task.Os.Install.SUSE',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'install-os-general.json',
    options: {
        osType: 'linux', //readonly options, should avoid change it
        profile: 'install-suse.ipxe',
        installScript: 'suse-autoinst.xml',
        installScriptUri: '{{ api.templates }}/{{ options.installScript }}?nodeId={{ task.nodeId }}', //jshint ignore: line
        hostname: 'localhost',
        comport: 'ttyS0',
        repo: '{{file.server}}/distribution/{{options.version}}/repo/oss/',
        rootPassword: "RackHDRocks!",
        kargs:{},

        //Some milestones are injected where can add custom commands.
        //Refer to below link for those injectable points:
        //http://users.suse.com/~ug/autoyast_doc/configuration.html#createprofile.scripts
        progressMilestones: {
            //jshint ignore: start
            requestProfile:     { value: 1, description: 'Enter ipxe and request OS installation profile' },
            enterProfile:       { value: 2, description: 'Enter profile, start to download installer'},
            startInstaller:     { value: 3, description: 'Start installer and prepare installation' },
            preConfig:          { value: 4, description: 'Enter Pre OS configuration'},
            postPartitioning:   { value: 5, description: 'Finished partitioning and mounting, start package installation'},
            chroot:             { value: 6, description: 'Finished package installation, start first boot'},
            // Now install SUSE task is designed to exist earlier before the postConfig milestone,
            // this is because the postConfig is triggered during OS first boot stage, but the first
            // boot will re-download the ipxe script and start a refresh installation rather than
            // continue the previous installation. This is the reason why the postConfig milestone
            // is removed.
            // This is a defect for RackHD, multiple reboot shouldn't occur during the life of the
            // same task.
            // TODO: Try to find a way to let RackHD supports multiple reboot during a task
            // execution. After then enable below postConfig milestone.
            //
            // postConfig:      { value: 7, description: 'Finished system configuration, and booted into system'},
            completed:          { value: 7, description: 'Finished OS installation'}
            //jshint ignore: end
        }
    },
    properties: {
        os: {
            linux: {
                distribution: 'suse'
            }
        }
    }
};
