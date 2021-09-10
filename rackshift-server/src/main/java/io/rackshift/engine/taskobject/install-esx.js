// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install Esx',
    injectableName: 'Task.Os.Install.ESXi',
    implementsTask: 'Task.Base.Os.Install',
    optionsSchema: 'install-esxi.json',
    options: {
        osType: 'esx', //readonly option, should avoid change it
        profile: 'install-esx.ipxe',
        installScript: 'esx-ks',
        installScriptUri: '{{ api.templates }}/{{ options.installScript }}?nodeId={{ task.nodeId }}', //jshint ignore: line
        rackhdCallbackScript: 'esx.rackhdcallback',
        esxBootConfigTemplate: 'esx-boot-cfg',
        esxBootConfigTemplateUri: '{{ api.templates }}/{{ options.esxBootConfigTemplate }}?nodeId={{ task.nodeId }}', //jshint ignore: line
        comport: 'com1',
        comportaddress: '0x3f8', //com1=0x3f8, com2=0x2f8, com3=0x3e8, com4=0x2e8
        gdbPort: 'default',
        logPort: 'com1',
        debugLogToSerial: '1',
        repo: '{{file.server}}/esxi/{{options.version}}',
        hostname: 'localhost',
        rootPassword: "RackHDRocks!",
        kargs: {},

        //jshint ignore: start
        //Some milestones are injected where can add custom commands.
        //Refer to below link for those injectable points:
        //https://kb.vmware.com/selfservice/microsites/search.do?language=en_US&cmd=displayKC&externalId=2004582
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
            hypervisor: {
                type: 'esx'
            }
        }
    }
};
