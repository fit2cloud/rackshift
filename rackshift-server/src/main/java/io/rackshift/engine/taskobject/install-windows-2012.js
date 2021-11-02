// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install Windows',
    injectableName: 'Task.Os.Install.Win',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'install-windows.json',
    options: {
        osType: 'windows', //readonly option, should avoid change it

        profile: 'windows.ipxe',
        hostname: 'localhost',
        domain: 'rackhd',
        password: "RackHDRocks!",
        username: "onrack",
        smbRepo: "\\\\{{ config.apiServerAddress }}\\windowsServer2012",// the samba mount point
        repo :'{{file.server}}/winpe',
        productkey: "XXXXX-XXXXX-XXXXX-XXXXX-XXXXX",
        firewallDisable: false,
        progressMilestones: {
            requestProfile: { value: 1, description: 'Enter ipxe and request OS installation profile' }, //jshint ignore: line
            enterProfile:   { value: 2, description: 'Enter profile, start to download WinPE'},
            startInstaller: { value: 3, description: 'Boot WinPE' },
            startSetup:     { value: 4, description: 'Net use Windows Server 2012 and start setup.exe' }, //jshint ignore: line
            completed:      { value: 5, description: 'Finished OS installation'}
        }
    },
    properties: {
        os: {
            windows: {
                type: 'server'
            }
        }
    }
};
