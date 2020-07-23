package io.rackshift.metal.sdk.constants;

import java.util.List;
import java.util.regex.Pattern;

public class BareMetalConstants {

    //物理机型号正则匹配 用于获取信息从machinelist查找合适的未分配的机器
    public static Pattern pmPattern = Pattern.compile("(.+)\\((\\d+)U(\\d+)核(\\d+)T(\\d+)G\\)");

    //1：允许分配给用户 0：不允许分配给用户
    public static final byte SUPPORT_AUTO = 1;
    public static final byte NOT_SUPPORT_AUTO = 0;

    //1：允许安装系统 0：不允许安装系统
    public static final byte ALLOW_INSTALL_OS = 1;
    public static final byte NOT_ALLOW_OS = 0;


    public enum BareMetalRuleSyncStatus {
        PENDING, SYNCING, DONE, ERROR
    }

    //带外信息来源
    public static final byte BY_HAND = 1;//手动添加
    public static final byte BY_IMPORT = 2;//导入
    public static final byte BY_RACKHD = 3;//RackHD主动发现
    public static final byte BY_RACKHD_SMI = 4;//RackHD扫描纳管（表示ipmi能通，能获取纳管信息）
    public static final byte BY_RACKHD_SMI_SENSE = 5;//RackHD扫描感知（表示ipmi不能连通，账号密码错误或带外异常，但是ping可以ping通）

    public static final int SYNC_SUCCESS = 1;
    public static final int SYNC_FAIL = 0;

    //硬件爬虫硬件状态
    public static final byte EXISTS = 0;
    public static final byte NEW_INSERT = 1;
    public static final byte DELETED = 2;

    public enum BareMetalIsManage {
        YES, NO
    }

    //监控硬件状态
    public static final Integer ERROR = 1;//所有非健康的情况包括 错误，可预测性故障（比如阵列更换数据盘）
    public static final Integer HEALTHY = 0;
    public static final Integer NOTDETECTED = -1;//未检测到

    //物理机开关机状态 开，关，未知
    public static final String PM_POWER_ON = "on";
    public static final String PM_POWER_OFF = "off";
    public static final String PM_POWER_UNKNOWN = "unknown";
}
