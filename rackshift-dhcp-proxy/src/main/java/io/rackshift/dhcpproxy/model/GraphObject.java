package io.rackshift.dhcpproxy.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GraphObject implements Serializable {


    /**
     * _id : {"$oid":"611f62f3e917b797eafd26e2"}
     * definition : {"friendlyName":"Install CentOS","injectableName":"Graph.InstallCentOS","options":{"install-os":{"version":null,"_taskTimeout":3600000},"rackhd-callback-notification-wait":{"_taskTimeout":1200000},"defaults":{"hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift"},"_taskTimeout":7200000},"tasks":[{"label":"set-boot-pxe","taskName":"Task.Obm.Node.PxeBoot","ignoreFailure":true},{"label":"reboot","taskName":"Task.Obm.Node.Reboot","waitOn":{"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c":["finished"]}},{"label":"install-os","taskName":"Task.Os.Install.CentOS","waitOn":{"abfaa92f-9a4e-42be-8992-fb3ed91d5939":"succeeded"}},{"label":"rackhd-callback-notification-wait","taskName":"Task.Wait.Notification","waitOn":{"087b224e-aacf-420f-a6e4-0488dbc8e0e3":"succeeded"}}]}
     * instanceId : cc95417a-53ee-4cce-84c5-b9d6551d593a
     * serviceGraph : null
     * context : {"target":"61109dc2cb11b70100d95a7e","graphId":"cc95417a-53ee-4cce-84c5-b9d6551d593a","graphName":"Graph.InstallCentOS"}
     * domain : default
     * name : Install CentOS
     * injectableName : Graph.InstallCentOS
     * tasks : {"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c":{"friendlyName":"Set Node Pxeboot","injectableName":"Task.Obm.Node.PxeBoot","implementsTask":"Task.Base.Obm.Node","options":{"action":"setBootPxe","hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift","_taskTimeout":86400000},"properties":{"power":{}},"instanceId":"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c","runJob":"Job.Obm.Node","jobOptionsSchema":{"properties":{"action":{"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]},"obmService":{"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}},"required":["action"]},"label":"set-boot-pxe","name":"Task.Obm.Node.PxeBoot","waitingOn":{},"ignoreFailure":true,"state":"succeeded","terminalOnStates":[],"taskStartTime":{"$date":"2021-08-20T08:08:19.282Z"},"taskEndTime":{"$date":"2021-08-20T08:08:24.225Z"}},"abfaa92f-9a4e-42be-8992-fb3ed91d5939":{"friendlyName":"Reboot Node","injectableName":"Task.Obm.Node.Reboot","implementsTask":"Task.Base.Obm.Node","options":{"action":"reboot","hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift","_taskTimeout":86400000},"properties":{"power":{"state":"reboot"}},"instanceId":"abfaa92f-9a4e-42be-8992-fb3ed91d5939","runJob":"Job.Obm.Node","jobOptionsSchema":{"properties":{"action":{"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]},"obmService":{"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}},"required":["action"]},"label":"reboot","name":"Task.Obm.Node.Reboot","waitingOn":{"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c":["finished"]},"ignoreFailure":false,"state":"succeeded","terminalOnStates":["timeout","cancelled","failed"],"taskStartTime":{"$date":"2021-08-20T08:08:24.237Z"},"taskEndTime":{"$date":"2021-08-20T08:08:49.751Z"}},"087b224e-aacf-420f-a6e4-0488dbc8e0e3":{"friendlyName":"Install CentOS","injectableName":"Task.Os.Install.CentOS","implementsTask":"Task.Base.Os.Install","optionsSchema":"install-centos.json","options":{"osType":"linux","profile":"install-centos.ipxe","installScript":"centos-ks","installScriptUri":"http://192.168.1.3:9030/api/current/templates/centos-ks?nodeId=61109dc2cb11b70100d95a7e","hostname":"rackshift-proxy","comport":"ttyS0","rackhdCallbackScript":"centos.rackhdcallback","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","rootPassword":"RackShift","remoteLogging":false,"progressMilestones":{"requestProfile":{"value":1,"description":"Enter ipxe and request OS installation profile"},"enterProfile":{"value":2,"description":"Enter profile, start to download installer"},"startInstaller":{"value":3,"description":"Start installer and prepare installation"},"preConfig":{"value":4,"description":"Enter Pre OS configuration"},"postConfig":{"value":5,"description":"Enter Post OS configuration"},"completed":{"value":6,"description":"Finished OS installation"}},"networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"_taskTimeout":3600000},"properties":{"os":{"linux":{"distribution":"centos"},"type":"install"}},"instanceId":"087b224e-aacf-420f-a6e4-0488dbc8e0e3","runJob":"Job.Os.Install","jobOptionsSchema":null,"label":"install-os","name":"Task.Os.Install.CentOS","waitingOn":{"abfaa92f-9a4e-42be-8992-fb3ed91d5939":"succeeded"},"ignoreFailure":false,"state":"pending","terminalOnStates":["timeout","cancelled","failed"],"taskStartTime":{"$date":"2021-08-20T08:08:49.771Z"}},"959751b3-e298-46a4-bab8-22b3b84fd010":{"friendlyName":"Notification Trigger","injectableName":"Task.Wait.Notification","implementsTask":"Task.Base.Wait.Notification","options":{"_taskTimeout":1200000},"properties":{},"instanceId":"959751b3-e298-46a4-bab8-22b3b84fd010","runJob":"Job.Wait.Notification","jobOptionsSchema":null,"label":"rackhd-callback-notification-wait","name":"Task.Wait.Notification","waitingOn":{"087b224e-aacf-420f-a6e4-0488dbc8e0e3":"succeeded"},"ignoreFailure":false,"state":"pending","terminalOnStates":["succeeded","timeout","cancelled","failed"]}}
     * _status : running
     * logContext : {"graphInstance":"cc95417a-53ee-4cce-84c5-b9d6551d593a","graphName":"Install CentOS","id":"61109dc2cb11b70100d95a7e"}
     * node : {"$oid":"61109dc2cb11b70100d95a7e"}
     * updatedAt : {"$date":"2021-08-20T08:08:49.771Z"}
     * createdAt : {"$date":"2021-08-20T08:08:19.267Z"}
     */

    private IdBean _id;
    private DefinitionBean definition;
    private String instanceId;
    private Object serviceGraph;
    private ContextBean context;
    private String domain;
    private String name;
    private String injectableName;
    private TasksBeanX tasks;
    private String _status;
    private LogContextBean logContext;
    private NodeBean node;
    private UpdatedAtBean updatedAt;
    private CreatedAtBean createdAt;

    @Data
    public static class IdBean implements Serializable {
        /**
         * $oid : 611f62f3e917b797eafd26e2
         */

        private String $oid;
    }

    @Data
    public static class DefinitionBean implements Serializable {
        /**
         * friendlyName : Install CentOS
         * injectableName : Graph.InstallCentOS
         * options : {"install-os":{"version":null,"_taskTimeout":3600000},"rackhd-callback-notification-wait":{"_taskTimeout":1200000},"defaults":{"hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift"},"_taskTimeout":7200000}
         * tasks : [{"label":"set-boot-pxe","taskName":"Task.Obm.Node.PxeBoot","ignoreFailure":true},{"label":"reboot","taskName":"Task.Obm.Node.Reboot","waitOn":{"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c":["finished"]}},{"label":"install-os","taskName":"Task.Os.Install.CentOS","waitOn":{"abfaa92f-9a4e-42be-8992-fb3ed91d5939":"succeeded"}},{"label":"rackhd-callback-notification-wait","taskName":"Task.Wait.Notification","waitOn":{"087b224e-aacf-420f-a6e4-0488dbc8e0e3":"succeeded"}}]
         */

        private String friendlyName;
        private String injectableName;
        private OptionsBean options;
        private List<TasksBean> tasks;

        @Data
        public static class OptionsBean implements Serializable {
            /**
             * install-os : {"version":null,"_taskTimeout":3600000}
             * rackhd-callback-notification-wait : {"_taskTimeout":1200000}
             * defaults : {"hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift"}
             * _taskTimeout : 7200000
             */

            @SerializedName("install-os")
            private InstallOsBean installOs;
            @SerializedName("rackhd-callback-notification-wait")
            private RackhdCallbackNotificationWaitBean rackhdCallbackNotificationWait;
            private DefaultsBean defaults;
            private int _taskTimeout;

            @Data
            public static class InstallOsBean implements Serializable {
                /**
                 * version : null
                 * _taskTimeout : 3600000
                 */

                private Object version;
                private int _taskTimeout;
            }

            @Data
            public static class RackhdCallbackNotificationWaitBean implements Serializable {
                /**
                 * _taskTimeout : 1200000
                 */

                private int _taskTimeout;
            }

            @Data
            public static class DefaultsBean implements Serializable {
                /**
                 * hostname : rackshift-proxy
                 * networkDevices : [{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}]
                 * installDisk : /dev/sda
                 * repo : http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261
                 * version : 7
                 * postInstallCommands : ["echo install success > /root/proxy.log"]
                 * rootPassword : RackShift
                 */

                private String hostname;
                private String installDisk;
                private String repo;
                private String version;
                private String rootPassword;
                private List<NetworkDevicesBean> networkDevices;
                private List<String> postInstallCommands;

                @Data
                public static class NetworkDevicesBean implements Serializable {
                    /**
                     * ipv4 : {"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"}
                     * device : 6c:92:bf:b4:85:0a
                     */

                    private Ipv4Bean ipv4;
                    private String device;

                    @Data
                    public static class Ipv4Bean implements Serializable {
                        /**
                         * netmask : 255.255.255.0
                         * ipAddr : 192.168.1.120
                         * gateway : 192.168.1.1
                         */

                        private String netmask;
                        private String ipAddr;
                        private String gateway;
                    }
                }
            }
        }

        @Data
        public static class TasksBean implements Serializable {
            /**
             * label : set-boot-pxe
             * taskName : Task.Obm.Node.PxeBoot
             * ignoreFailure : true
             * waitOn : {"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c":["finished"]}
             */

            private String label;
            private String taskName;
            private boolean ignoreFailure;
            private WaitOnBean waitOn;

            @Data
            public static class WaitOnBean implements Serializable {
                @SerializedName("7e5e0786-65ff-4e89-93a0-e5bcb9b6589c")
                private List<String> _$7e5e078665ff4e8993a0E5bcb9b6589c;
            }
        }
    }

    @Data
    public static class ContextBean implements Serializable {
        /**
         * target : 61109dc2cb11b70100d95a7e
         * graphId : cc95417a-53ee-4cce-84c5-b9d6551d593a
         * graphName : Graph.InstallCentOS
         */

        private String target;
        private String graphId;
        private String graphName;
    }

    @Data
    public static class TasksBeanX implements Serializable {
        /**
         * 7e5e0786-65ff-4e89-93a0-e5bcb9b6589c : {"friendlyName":"Set Node Pxeboot","injectableName":"Task.Obm.Node.PxeBoot","implementsTask":"Task.Base.Obm.Node","options":{"action":"setBootPxe","hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift","_taskTimeout":86400000},"properties":{"power":{}},"instanceId":"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c","runJob":"Job.Obm.Node","jobOptionsSchema":{"properties":{"action":{"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]},"obmService":{"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}},"required":["action"]},"label":"set-boot-pxe","name":"Task.Obm.Node.PxeBoot","waitingOn":{},"ignoreFailure":true,"state":"succeeded","terminalOnStates":[],"taskStartTime":{"$date":"2021-08-20T08:08:19.282Z"},"taskEndTime":{"$date":"2021-08-20T08:08:24.225Z"}}
         * abfaa92f-9a4e-42be-8992-fb3ed91d5939 : {"friendlyName":"Reboot Node","injectableName":"Task.Obm.Node.Reboot","implementsTask":"Task.Base.Obm.Node","options":{"action":"reboot","hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift","_taskTimeout":86400000},"properties":{"power":{"state":"reboot"}},"instanceId":"abfaa92f-9a4e-42be-8992-fb3ed91d5939","runJob":"Job.Obm.Node","jobOptionsSchema":{"properties":{"action":{"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]},"obmService":{"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}},"required":["action"]},"label":"reboot","name":"Task.Obm.Node.Reboot","waitingOn":{"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c":["finished"]},"ignoreFailure":false,"state":"succeeded","terminalOnStates":["timeout","cancelled","failed"],"taskStartTime":{"$date":"2021-08-20T08:08:24.237Z"},"taskEndTime":{"$date":"2021-08-20T08:08:49.751Z"}}
         * 087b224e-aacf-420f-a6e4-0488dbc8e0e3 : {"friendlyName":"Install CentOS","injectableName":"Task.Os.Install.CentOS","implementsTask":"Task.Base.Os.Install","optionsSchema":"install-centos.json","options":{"osType":"linux","profile":"install-centos.ipxe","installScript":"centos-ks","installScriptUri":"http://192.168.1.3:9030/api/current/templates/centos-ks?nodeId=61109dc2cb11b70100d95a7e","hostname":"rackshift-proxy","comport":"ttyS0","rackhdCallbackScript":"centos.rackhdcallback","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","rootPassword":"RackShift","remoteLogging":false,"progressMilestones":{"requestProfile":{"value":1,"description":"Enter ipxe and request OS installation profile"},"enterProfile":{"value":2,"description":"Enter profile, start to download installer"},"startInstaller":{"value":3,"description":"Start installer and prepare installation"},"preConfig":{"value":4,"description":"Enter Pre OS configuration"},"postConfig":{"value":5,"description":"Enter Post OS configuration"},"completed":{"value":6,"description":"Finished OS installation"}},"networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"_taskTimeout":3600000},"properties":{"os":{"linux":{"distribution":"centos"},"type":"install"}},"instanceId":"087b224e-aacf-420f-a6e4-0488dbc8e0e3","runJob":"Job.Os.Install","jobOptionsSchema":null,"label":"install-os","name":"Task.Os.Install.CentOS","waitingOn":{"abfaa92f-9a4e-42be-8992-fb3ed91d5939":"succeeded"},"ignoreFailure":false,"state":"pending","terminalOnStates":["timeout","cancelled","failed"],"taskStartTime":{"$date":"2021-08-20T08:08:49.771Z"}}
         * 959751b3-e298-46a4-bab8-22b3b84fd010 : {"friendlyName":"Notification Trigger","injectableName":"Task.Wait.Notification","implementsTask":"Task.Base.Wait.Notification","options":{"_taskTimeout":1200000},"properties":{},"instanceId":"959751b3-e298-46a4-bab8-22b3b84fd010","runJob":"Job.Wait.Notification","jobOptionsSchema":null,"label":"rackhd-callback-notification-wait","name":"Task.Wait.Notification","waitingOn":{"087b224e-aacf-420f-a6e4-0488dbc8e0e3":"succeeded"},"ignoreFailure":false,"state":"pending","terminalOnStates":["succeeded","timeout","cancelled","failed"]}
         */

        @SerializedName("7e5e0786-65ff-4e89-93a0-e5bcb9b6589c")
        private _$7e5e078665ff4e8993a0E5bcb9b6589cBean _$7e5e078665ff4e8993a0E5bcb9b6589c;
        @SerializedName("abfaa92f-9a4e-42be-8992-fb3ed91d5939")
        private Abfaa92f9a4e42be8992Fb3ed91d5939Bean abfaa92f9a4e42be8992Fb3ed91d5939;
        @SerializedName("087b224e-aacf-420f-a6e4-0488dbc8e0e3")
        private _$087b224eAacf420fA6e40488dbc8e0e3Bean _$087b224eAacf420fA6e40488dbc8e0e3;
        @SerializedName("959751b3-e298-46a4-bab8-22b3b84fd010")
        private _$959751b3E29846a4Bab822b3b84fd010Bean _$959751b3E29846a4Bab822b3b84fd010;

        @Data
        public static class _$7e5e078665ff4e8993a0E5bcb9b6589cBean implements Serializable {
            /**
             * friendlyName : Set Node Pxeboot
             * injectableName : Task.Obm.Node.PxeBoot
             * implementsTask : Task.Base.Obm.Node
             * options : {"action":"setBootPxe","hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift","_taskTimeout":86400000}
             * properties : {"power":{}}
             * instanceId : 7e5e0786-65ff-4e89-93a0-e5bcb9b6589c
             * runJob : Job.Obm.Node
             * jobOptionsSchema : {"properties":{"action":{"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]},"obmService":{"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}},"required":["action"]}
             * label : set-boot-pxe
             * name : Task.Obm.Node.PxeBoot
             * waitingOn : {}
             * ignoreFailure : true
             * state : succeeded
             * terminalOnStates : []
             * taskStartTime : {"$date":"2021-08-20T08:08:19.282Z"}
             * taskEndTime : {"$date":"2021-08-20T08:08:24.225Z"}
             */

            private String friendlyName;
            private String injectableName;
            private String implementsTask;
            private OptionsBeanX options;
            private PropertiesBean properties;
            private String instanceId;
            private String runJob;
            private JobOptionsSchemaBean jobOptionsSchema;
            private String label;
            private String name;
            private WaitingOnBean waitingOn;
            private boolean ignoreFailure;
            private String state;
            private TaskStartTimeBean taskStartTime;
            private TaskEndTimeBean taskEndTime;
            private List<?> terminalOnStates;

            @Data
            public static class OptionsBeanX implements Serializable {
                /**
                 * action : setBootPxe
                 * hostname : rackshift-proxy
                 * networkDevices : [{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}]
                 * installDisk : /dev/sda
                 * repo : http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261
                 * version : 7
                 * postInstallCommands : ["echo install success > /root/proxy.log"]
                 * rootPassword : RackShift
                 * _taskTimeout : 86400000
                 */

                private String action;
                private String hostname;
                private String installDisk;
                private String repo;
                private String version;
                private String rootPassword;
                private int _taskTimeout;
                private List<NetworkDevicesBeanX> networkDevices;
                private List<String> postInstallCommands;

                @Data
                public static class NetworkDevicesBeanX implements Serializable {
                    /**
                     * ipv4 : {"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"}
                     * device : 6c:92:bf:b4:85:0a
                     */

                    private Ipv4BeanX ipv4;
                    private String device;

                    @Data
                    public static class Ipv4BeanX implements Serializable {
                        /**
                         * netmask : 255.255.255.0
                         * ipAddr : 192.168.1.120
                         * gateway : 192.168.1.1
                         */

                        private String netmask;
                        private String ipAddr;
                        private String gateway;
                    }
                }
            }

            @Data
            public static class PropertiesBean implements Serializable {
                /**
                 * power : {}
                 */

                private PowerBean power;

                @Data
                public static class PowerBean implements Serializable {
                }
            }

            @Data
            public static class JobOptionsSchemaBean implements Serializable {
                @Data
                public static class PropertiesBeanX implements Serializable {
                    @Data
                    public static class ActionBean implements Serializable {
                        @SerializedName("enum")
                        private List<String> enumX;
                    }

                    @Data
                    public static class ObmServiceBean implements Serializable {
                    }
                }
            }

            @Data
            public static class WaitingOnBean implements Serializable {
            }

            @Data
            public static class TaskStartTimeBean implements Serializable {
                /**
                 * $date : 2021-08-20T08:08:19.282Z
                 */

                private String $date;
            }

            @Data
            public static class TaskEndTimeBean implements Serializable {
                /**
                 * $date : 2021-08-20T08:08:24.225Z
                 */

                private String $date;
            }
        }

        @Data
        public static class Abfaa92f9a4e42be8992Fb3ed91d5939Bean implements Serializable {
            /**
             * friendlyName : Reboot Node
             * injectableName : Task.Obm.Node.Reboot
             * implementsTask : Task.Base.Obm.Node
             * options : {"action":"reboot","hostname":"rackshift-proxy","networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"rootPassword":"RackShift","_taskTimeout":86400000}
             * properties : {"power":{"state":"reboot"}}
             * instanceId : abfaa92f-9a4e-42be-8992-fb3ed91d5939
             * runJob : Job.Obm.Node
             * jobOptionsSchema : {"properties":{"action":{"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]},"obmService":{"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}},"required":["action"]}
             * label : reboot
             * name : Task.Obm.Node.Reboot
             * waitingOn : {"7e5e0786-65ff-4e89-93a0-e5bcb9b6589c":["finished"]}
             * ignoreFailure : false
             * state : succeeded
             * terminalOnStates : ["timeout","cancelled","failed"]
             * taskStartTime : {"$date":"2021-08-20T08:08:24.237Z"}
             * taskEndTime : {"$date":"2021-08-20T08:08:49.751Z"}
             */

            private String friendlyName;
            private String injectableName;
            private String implementsTask;
            private OptionsBeanXX options;
            private PropertiesBeanXX properties;
            private String instanceId;
            private String runJob;
            private JobOptionsSchemaBeanX jobOptionsSchema;
            private String label;
            private String name;
            private WaitingOnBeanX waitingOn;
            private boolean ignoreFailure;
            private String state;
            private TaskStartTimeBeanX taskStartTime;
            private TaskEndTimeBeanX taskEndTime;
            private List<String> terminalOnStates;

            @Data
            public static class OptionsBeanXX implements Serializable {
                /**
                 * action : reboot
                 * hostname : rackshift-proxy
                 * networkDevices : [{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}]
                 * installDisk : /dev/sda
                 * repo : http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261
                 * version : 7
                 * postInstallCommands : ["echo install success > /root/proxy.log"]
                 * rootPassword : RackShift
                 * _taskTimeout : 86400000
                 */

                private String action;
                private String hostname;
                private String installDisk;
                private String repo;
                private String version;
                private String rootPassword;
                private int _taskTimeout;
                private List<NetworkDevicesBeanXX> networkDevices;
                private List<String> postInstallCommands;

                @Data
                public static class NetworkDevicesBeanXX implements Serializable {
                    /**
                     * ipv4 : {"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"}
                     * device : 6c:92:bf:b4:85:0a
                     */

                    private Ipv4BeanXX ipv4;
                    private String device;

                    @Data
                    public static class Ipv4BeanXX implements Serializable {
                        /**
                         * netmask : 255.255.255.0
                         * ipAddr : 192.168.1.120
                         * gateway : 192.168.1.1
                         */

                        private String netmask;
                        private String ipAddr;
                        private String gateway;
                    }
                }
            }

            @Data
            public static class PropertiesBeanXX implements Serializable {
                /**
                 * power : {"state":"reboot"}
                 */

                private PowerBeanX power;

                @Data
                public static class PowerBeanX implements Serializable {
                    /**
                     * state : reboot
                     */

                    private String state;
                }
            }

            @Data
            public static class JobOptionsSchemaBeanX implements Serializable {
                /**
                 * properties : {"action":{"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]},"obmService":{"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}}
                 * required : ["action"]
                 */

                private PropertiesBeanXXX properties;
                private List<String> required;

                @Data
                public static class PropertiesBeanXXX implements Serializable {
                    /**
                     * action : {"enum":["clearSEL","identifyOff","identifyOn","identifyBlink","mcResetCold","NMI","powerButton","powerOff","powerOn","powerStatus","reboot","reset","setBootPxe","softReset","forceBootPxe","clearWatchDog","setBootDisk"]}
                     * obmService : {"enum":["amt-obm-service","apc-obm-service","ipmi-obm-service","noop-obm-service","panduit-obm-service","raritan-obm-service","redfish-obm-service","servertech-obm-service","vbox-obm-service","vmrun-obm-service","ucs-obm-service","dell-wsman-obm-service"]}
                     */

                    private ActionBeanX action;
                    private ObmServiceBeanX obmService;

                    @Data
                    public static class ActionBeanX implements Serializable {
                        @SerializedName("enum")
                        private List<String> enumX;
                    }

                    @Data
                    public static class ObmServiceBeanX implements Serializable {
                        @SerializedName("enum")
                        private List<String> enumX;
                    }
                }
            }

            @Data
            public static class WaitingOnBeanX implements Serializable {
                @SerializedName("7e5e0786-65ff-4e89-93a0-e5bcb9b6589c")
                private List<String> _$7e5e078665ff4e8993a0E5bcb9b6589c;
            }

            @Data
            public static class TaskStartTimeBeanX implements Serializable {
                /**
                 * $date : 2021-08-20T08:08:24.237Z
                 */

                private String $date;
            }

            @Data
            public static class TaskEndTimeBeanX implements Serializable {
                /**
                 * $date : 2021-08-20T08:08:49.751Z
                 */

                private String $date;
            }
        }

        @Data
        public static class _$087b224eAacf420fA6e40488dbc8e0e3Bean implements Serializable {
            /**
             * friendlyName : Install CentOS
             * injectableName : Task.Os.Install.CentOS
             * implementsTask : Task.Base.Os.Install
             * optionsSchema : install-centos.json
             * options : {"osType":"linux","profile":"install-centos.ipxe","installScript":"centos-ks","installScriptUri":"http://192.168.1.3:9030/api/current/templates/centos-ks?nodeId=61109dc2cb11b70100d95a7e","hostname":"rackshift-proxy","comport":"ttyS0","rackhdCallbackScript":"centos.rackhdcallback","repo":"http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261","rootPassword":"RackShift","remoteLogging":false,"progressMilestones":{"requestProfile":{"value":1,"description":"Enter ipxe and request OS installation profile"},"enterProfile":{"value":2,"description":"Enter profile, start to download installer"},"startInstaller":{"value":3,"description":"Start installer and prepare installation"},"preConfig":{"value":4,"description":"Enter Pre OS configuration"},"postConfig":{"value":5,"description":"Enter Post OS configuration"},"completed":{"value":6,"description":"Finished OS installation"}},"networkDevices":[{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}],"installDisk":"/dev/sda","version":"7","postInstallCommands":["echo install success > /root/proxy.log"],"_taskTimeout":3600000}
             * properties : {"os":{"linux":{"distribution":"centos"},"type":"install"}}
             * instanceId : 087b224e-aacf-420f-a6e4-0488dbc8e0e3
             * runJob : Job.Os.Install
             * jobOptionsSchema : null
             * label : install-os
             * name : Task.Os.Install.CentOS
             * waitingOn : {"abfaa92f-9a4e-42be-8992-fb3ed91d5939":"succeeded"}
             * ignoreFailure : false
             * state : pending
             * terminalOnStates : ["timeout","cancelled","failed"]
             * taskStartTime : {"$date":"2021-08-20T08:08:49.771Z"}
             */

            private String friendlyName;
            private String injectableName;
            private String implementsTask;
            private String optionsSchema;
            private OptionsBeanXXX options;
            private PropertiesBeanXXXX properties;
            private String instanceId;
            private String runJob;
            private Object jobOptionsSchema;
            private String label;
            private String name;
            private WaitingOnBeanXX waitingOn;
            private boolean ignoreFailure;
            private String state;
            private TaskStartTimeBeanXX taskStartTime;
            private List<String> terminalOnStates;

            @Data
            public static class OptionsBeanXXX implements Serializable {
                /**
                 * osType : linux
                 * profile : install-centos.ipxe
                 * installScript : centos-ks
                 * installScriptUri : http://192.168.1.3:9030/api/current/templates/centos-ks?nodeId=61109dc2cb11b70100d95a7e
                 * hostname : rackshift-proxy
                 * comport : ttyS0
                 * rackhdCallbackScript : centos.rackhdcallback
                 * repo : http://192.168.1.3:9090/common/CentOS-7-x86_64-Minimal-2003-rackshift1261
                 * rootPassword : RackShift
                 * remoteLogging : false
                 * progressMilestones : {"requestProfile":{"value":1,"description":"Enter ipxe and request OS installation profile"},"enterProfile":{"value":2,"description":"Enter profile, start to download installer"},"startInstaller":{"value":3,"description":"Start installer and prepare installation"},"preConfig":{"value":4,"description":"Enter Pre OS configuration"},"postConfig":{"value":5,"description":"Enter Post OS configuration"},"completed":{"value":6,"description":"Finished OS installation"}}
                 * networkDevices : [{"ipv4":{"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"},"device":"6c:92:bf:b4:85:0a"}]
                 * installDisk : /dev/sda
                 * version : 7
                 * postInstallCommands : ["echo install success > /root/proxy.log"]
                 * _taskTimeout : 3600000
                 */

                private String osType;
                private String profile;
                private String installScript;
                private String installScriptUri;
                private String hostname;
                private String comport;
                private String rackhdCallbackScript;
                private String repo;
                private String rootPassword;
                private boolean remoteLogging;
                private ProgressMilestonesBean progressMilestones;
                private String installDisk;
                private String version;
                private int _taskTimeout;
                private List<NetworkDevicesBeanXXX> networkDevices;
                private List<String> postInstallCommands;

                @Data
                public static class ProgressMilestonesBean implements Serializable {
                    /**
                     * requestProfile : {"value":1,"description":"Enter ipxe and request OS installation profile"}
                     * enterProfile : {"value":2,"description":"Enter profile, start to download installer"}
                     * startInstaller : {"value":3,"description":"Start installer and prepare installation"}
                     * preConfig : {"value":4,"description":"Enter Pre OS configuration"}
                     * postConfig : {"value":5,"description":"Enter Post OS configuration"}
                     * completed : {"value":6,"description":"Finished OS installation"}
                     */

                    private RequestProfileBean requestProfile;
                    private EnterProfileBean enterProfile;
                    private StartInstallerBean startInstaller;
                    private PreConfigBean preConfig;
                    private PostConfigBean postConfig;
                    private CompletedBean completed;

                    @Data
                    public static class RequestProfileBean implements Serializable {
                        /**
                         * value : 1
                         * description : Enter ipxe and request OS installation profile
                         */

                        private int value;
                        private String description;
                    }

                    @Data
                    public static class EnterProfileBean implements Serializable {
                        /**
                         * value : 2
                         * description : Enter profile, start to download installer
                         */

                        private int value;
                        private String description;
                    }

                    @Data
                    public static class StartInstallerBean implements Serializable {
                        /**
                         * value : 3
                         * description : Start installer and prepare installation
                         */

                        private int value;
                        private String description;
                    }

                    @Data
                    public static class PreConfigBean implements Serializable {
                        /**
                         * value : 4
                         * description : Enter Pre OS configuration
                         */

                        private int value;
                        private String description;
                    }

                    @Data
                    public static class PostConfigBean implements Serializable {
                        /**
                         * value : 5
                         * description : Enter Post OS configuration
                         */

                        private int value;
                        private String description;
                    }

                    @Data
                    public static class CompletedBean implements Serializable {
                        /**
                         * value : 6
                         * description : Finished OS installation
                         */

                        private int value;
                        private String description;
                    }
                }

                @Data
                public static class NetworkDevicesBeanXXX implements Serializable {
                    /**
                     * ipv4 : {"netmask":"255.255.255.0","ipAddr":"192.168.1.120","gateway":"192.168.1.1"}
                     * device : 6c:92:bf:b4:85:0a
                     */

                    private Ipv4BeanXXX ipv4;
                    private String device;

                    @Data
                    public static class Ipv4BeanXXX implements Serializable {
                        /**
                         * netmask : 255.255.255.0
                         * ipAddr : 192.168.1.120
                         * gateway : 192.168.1.1
                         */

                        private String netmask;
                        private String ipAddr;
                        private String gateway;
                    }
                }
            }

            @Data
            public static class PropertiesBeanXXXX implements Serializable {
                /**
                 * os : {"linux":{"distribution":"centos"},"type":"install"}
                 */

                private OsBean os;

                @Data
                public static class OsBean implements Serializable {
                    /**
                     * linux : {"distribution":"centos"}
                     * type : install
                     */

                    private LinuxBean linux;
                    private String type;

                    @Data
                    public static class LinuxBean implements Serializable {
                        /**
                         * distribution : centos
                         */

                        private String distribution;
                    }
                }
            }

            @Data
            public static class WaitingOnBeanXX implements Serializable {
                /**
                 * abfaa92f-9a4e-42be-8992-fb3ed91d5939 : succeeded
                 */

                @SerializedName("abfaa92f-9a4e-42be-8992-fb3ed91d5939")
                private String abfaa92f9a4e42be8992Fb3ed91d5939;
            }

            @Data
            public static class TaskStartTimeBeanXX implements Serializable {
                /**
                 * $date : 2021-08-20T08:08:49.771Z
                 */

                private String $date;
            }
        }

        @Data
        public static class _$959751b3E29846a4Bab822b3b84fd010Bean implements Serializable {
            /**
             * friendlyName : Notification Trigger
             * injectableName : Task.Wait.Notification
             * implementsTask : Task.Base.Wait.Notification
             * options : {"_taskTimeout":1200000}
             * properties : {}
             * instanceId : 959751b3-e298-46a4-bab8-22b3b84fd010
             * runJob : Job.Wait.Notification
             * jobOptionsSchema : null
             * label : rackhd-callback-notification-wait
             * name : Task.Wait.Notification
             * waitingOn : {"087b224e-aacf-420f-a6e4-0488dbc8e0e3":"succeeded"}
             * ignoreFailure : false
             * state : pending
             * terminalOnStates : ["succeeded","timeout","cancelled","failed"]
             */

            private String friendlyName;
            private String injectableName;
            private String implementsTask;
            private OptionsBeanXXXX options;
            private PropertiesBeanXXXXX properties;
            private String instanceId;
            private String runJob;
            private Object jobOptionsSchema;
            private String label;
            private String name;
            private WaitingOnBeanXXX waitingOn;
            private boolean ignoreFailure;
            private String state;
            private List<String> terminalOnStates;

            @Data
            public static class OptionsBeanXXXX implements Serializable {
            }

            @Data
            public static class PropertiesBeanXXXXX implements Serializable {
            }

            @Data
            public static class WaitingOnBeanXXX implements Serializable {
                /**
                 * 087b224e-aacf-420f-a6e4-0488dbc8e0e3 : succeeded
                 */

                @SerializedName("087b224e-aacf-420f-a6e4-0488dbc8e0e3")
                private String _$087b224eAacf420fA6e40488dbc8e0e3;
            }
        }
    }

    @Data
    public static class LogContextBean implements Serializable {
        /**
         * graphInstance : cc95417a-53ee-4cce-84c5-b9d6551d593a
         * graphName : Install CentOS
         * id : 61109dc2cb11b70100d95a7e
         */

        private String graphInstance;
        private String graphName;
        private String id;
    }

    @Data
    public static class NodeBean implements Serializable {
        /**
         * $oid : 61109dc2cb11b70100d95a7e
         */

        private String $oid;
    }

    @Data
    public static class UpdatedAtBean implements Serializable {
        /**
         * $date : 2021-08-20T08:08:49.771Z
         */

        private String $date;
    }

    @Data
    public static class CreatedAtBean implements Serializable {
        /**
         * $date : 2021-08-20T08:08:19.267Z
         */

        private String $date;
    }
}

