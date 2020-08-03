package io.rackshift.strategy.statemachine;

public enum LifeStatus {


    onrack("onrack"),//新上架机器，被发现协议新发现
    provisioning("provisioning"),//执行工作流任务中
    discovering("discovering"),//执行发现任务中
    deploying("deploying"),//安装系统中
    ready("ready"),//发现任务结束
    allocated("allocated"),//已经分配完毕
    deployed("deployed");//已经装完系统
    private String status;

    LifeStatus(String status) {
        this.status = status;
    }

    LifeStatus fromString(String status) {
        return LifeStatus.valueOf(status);
    }

    public static LifeStatus[] allValue() {
        return LifeStatus.values();
    }

}
