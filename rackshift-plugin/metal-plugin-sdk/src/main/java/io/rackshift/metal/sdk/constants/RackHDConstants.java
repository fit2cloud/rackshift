package io.rackshift.metal.sdk.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * rackhd 常量配置
 */
public class RackHDConstants {
    public static final String NODES_URL = "/api/current/nodes/";
    public static final String NODES_OBM_URL = "/api/current/nodes/%s/obm";
    public static final String CANCEL_WORKFLOW_URL = "/api/current/nodes/%s/workflows/action";
    public static final String GET_WORKFLOW_URL = "/api/2.0/workflows";
    public static final String workflowPostUrl = "/api/2.0/nodes/%s/workflows?name=";

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
}
