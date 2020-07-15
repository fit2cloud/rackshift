package io.rackshift.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * rackhd 常量配置
 */
public class RackHDConstants {
    public static final String NODES_URL = "/api/current/nodes/";
    public static final String CATALOG_URL = "/api/2.0/nodes/%s/catalogs";
    public static final String NODES_OBM_URL = "/api/current/nodes/%s/obm";
    public static final String CANCEL_WORKFLOW_URL = "/api/current/nodes/%s/workflows/action";
    public static final String GET_WORKFLOW_URL = "/api/2.0/workflows";
    private static final String WORKFLOW_POWER_ON = "Graph.PowerOn.Node";
    private static final String WORKFLOW_POWER_OFF = "Graph.PowerOff.Node";
    private static final String WORKFLOW_POWER_RESET = "Graph.Reboot.Node";
    private static final String WORKFLOW_INSTALL_CENTOS = "Graph.InstallCentOS";
    private static final String WORKFLOW_INSTALL_WINDOWS = "Graph.InstallWindowsServer";

    private static final String WORKFLOW_RAID = "Graph.Raid.Create.MegaRAID";
    private static final String DELL_WORKFLOW_RAID = "Graph.Raid.Create.PercRAID";
    private static final String HP_WORKFLOW_RAID = "Graph.Raid.Create.HpssaRAID";
    private static final String WORKFLOW_PERCCLI_CATALOG = "Graph.Dell.perccli.Catalog";
    private static final String WORKFLOW_STORCIL_CATALOG = "Graph.Quanta.storcli.Catalog";
    private static final String WORKFLOW_HPSSACIL_CATALOG = "Graph.HP.ssacli.Catalog";
    private static final String WORKFLOW_DELETE_RAID = "Graph.Raid.Delete.MegaRAID";
    private static final String HP_WORKFLOW_DELETE_RAID = "Graph.Raid.Delete.HpssaRAID";


    //物理机开关机状态 开，关，未知
    public static final String PM_POWER_ON = "on";
    public static final String PM_POWER_OFF = "off";
    public static final String PM_POWER_UNKNOWN = "unknown";
    //物理机带外是否在线状态（只要能通过ipmi调通则表示是在线状态，不管开机没开机）开，关
    public static final String PM_OUT_BAND_ON = "on";//用户层面体现为“在线”状态
    public static final String PM_OUT_BAND_CANNOT_PING = "on-no-ping"; //用户层面体现为“在线”状态 ,逻辑上区分开来，后台逻辑做区别处理 标识南方中心宿主机只有rest账号可以登录，ping和ipmi都不能联通
    public static final String PM_OUT_BAND_OFF = "off";
    public static final String PM_OUT_BAND_UNKNOW = "unknown";
    public static final String PM_OUT_BAND_UNKNOW_IP = "unknown-ip";

    public static final Integer ERROR_RE_CODE = 300;
    public static Map<String, String> powerOptMap = new HashMap() {{
        put("on", WORKFLOW_POWER_ON);
        put("off", WORKFLOW_POWER_OFF);
        put("reset", WORKFLOW_POWER_RESET);
    }};

    public enum WorkFlowEnum {
        POWER_ON(WORKFLOW_POWER_ON, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_POWER_ON),
        POWER_OFF(WORKFLOW_POWER_OFF, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_POWER_OFF),
        POWER_RESET(WORKFLOW_POWER_RESET, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_POWER_RESET),
        INSTALL_CENTOS(WORKFLOW_INSTALL_CENTOS, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_INSTALL_CENTOS),
        INSTALL_WINDOWS(WORKFLOW_INSTALL_WINDOWS, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_INSTALL_WINDOWS),
        PERCCLI_CATALOG(WORKFLOW_PERCCLI_CATALOG, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_PERCCLI_CATALOG),
        STORCIL_CATALOG(WORKFLOW_STORCIL_CATALOG, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_STORCIL_CATALOG),
        HPSSACIL_CATALOG(WORKFLOW_HPSSACIL_CATALOG, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_HPSSACIL_CATALOG),
        RAID(WORKFLOW_RAID, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_RAID),
        DELL_RAID(DELL_WORKFLOW_RAID, "/api/2.0/nodes/%s/workflows?name=" + DELL_WORKFLOW_RAID),
        HP_RAID(HP_WORKFLOW_RAID, "/api/2.0/nodes/%s/workflows?name=" + HP_WORKFLOW_RAID),
        DELETE_RAID(WORKFLOW_DELETE_RAID, "/api/2.0/nodes/%s/workflows?name=" + WORKFLOW_DELETE_RAID),
        HP_DELETE_RAID(HP_WORKFLOW_DELETE_RAID, "/api/2.0/nodes/%s/workflows?name=" + HP_WORKFLOW_DELETE_RAID);


        private String workflow;
        private String workflowUrl;

        WorkFlowEnum(String workflow, String workflowUrl) {
            this.workflow = workflow;
            this.workflowUrl = workflowUrl;
        }

        public String getWorkflow() {
            return workflow;
        }

        public void setWorkflow(String workflow) {
            this.workflow = workflow;
        }

        public String getWorkflowUrl() {
            return workflowUrl;
        }

        public void setWorkflowUrl(String workflowUrl) {
            this.workflowUrl = workflowUrl;
        }

        public static WorkFlowEnum findByWorkFlow(String workflow) {
            return Arrays.stream(WorkFlowEnum.values()).filter(w -> w.workflow.equalsIgnoreCase(workflow)).findFirst().orElse(null);
        }

    }
}
