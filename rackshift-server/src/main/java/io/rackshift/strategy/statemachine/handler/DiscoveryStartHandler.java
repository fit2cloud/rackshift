package io.rackshift.strategy.statemachine.handler;

import io.rackshift.job.SyncRackJob;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.manager.OutBandManager;
import io.rackshift.model.RSException;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.AbstractHandler;
import io.rackshift.strategy.statemachine.EventHandlerAnnotation;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.IPMIUtil;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEvent.POST_DISCOVERY_WORKFLOW_START)
public class DiscoveryStartHandler extends AbstractHandler {
    @Resource
    private RackHDService rackHDService;
    @Resource
    private OutBandManager outBandManager;
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private SyncRackJob syncRackJob;
    @Resource
    private String rackhdUrl;

    /**
     * 执行发现分为两种情况
     * 1.手动添加的物理机未被rackhd发现
     * 执行发现的workflow：先PXE启动再轮询找到该机器的节点信息，最后刷新
     * 2.已经执行过一次完整的信息采集的机器
     * 先pxe启动rancher再执行发现workflow
     *
     * @param event
     */
    @Override
    public void handleMyself(LifeEvent event) {

        WorkflowRequestDTO requestDTO = event.getParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());
        OutBand o = outBandManager.getByIp(bareMetal.getManagementIp()).get(0);
        String originStatus = bareMetal.getStatus();
        beforeChange(LifeStatus.valueOf(originStatus));
        if (StringUtils.isBlank(bareMetal.getServerId())) {
            try {
                IPMIUtil.Account account = IPMIUtil.Account.build(o);
                IPMIUtil.exeCommand(account, "chassis bootdev pxe");
                IPMIUtil.exeCommand(account, "power off");
                IPMIUtil.exeCommand(account, "power on");
                bareMetal.setStatus(LifeStatus.discovering.name());
                bareMetalManager.update(bareMetal, true);
            } catch (Exception e) {
                RSException.throwExceptions(e.getMessage());
            }
        } else {
            try {
                //清空之前所有正在运行的任务
                rackHDService.clearActiveWorkflow(bareMetal.getServerId());
                boolean result = rackHDService.postWorkflow(rackhdUrl, bareMetal.getServerId(), "Graph.BootstrapRancher", null);
                if (result) {
                    result = rackHDService.postWorkflow(rackhdUrl, bareMetal.getServerId(), "Graph.Discovery", null);
                    //同步执行一次最新的发现
                    if (result) {
                        syncRackJob.run();
                    }
                    bareMetal.setStatus(LifeStatus.ready.name());
                    bareMetalManager.update(bareMetal, true);
                } else {
                    RSException.throwExceptions(Translator.get("i18n_state_error"));
                }
            } catch (Exception e) {
                bareMetal.setStatus(originStatus);
                bareMetalManager.update(bareMetal, true);
                RSException.throwExceptions(e.getMessage());
            }
        }
    }
}
