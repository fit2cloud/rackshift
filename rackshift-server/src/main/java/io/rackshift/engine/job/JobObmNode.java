package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.CatalogExample;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.mapper.CatalogMapper;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.service.OutBandService;
import io.rackshift.utils.IPMIUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@Jobs("Job.Obm.Node")
public class JobObmNode extends BaseJob {
    public JobObmNode() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobObmNode(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
        this.instanceId = instanceId;
        this.taskId = taskId;
        this.context = context;
        this.options = context.getJSONObject("options");
        this._status = context.getString("state");
        this.taskMapper = taskMapper;
        this.task = taskMapper.selectByPrimaryKey(taskId);
        this.bareMetalId = context.getString("bareMetalId");
        this.applicationContext = applicationContext;
        this.job = (Map<String, Class>) applicationContext.getBean("job");
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run() {
        String action = this.options.getString("action");
        try {
            new OBMService(action).run();
            this.succeeded();
        } catch (Exception e) {
            this.error(e);
        }
    }

    private class OBMService {
        private String action;

        private OBMService(String action) {
            this.action = action;
        }

        private void run() throws Exception {
            OutBandService outBandService = (OutBandService) applicationContext.getBean("outBandService");
            OutBand outBand = outBandService.getByBareMetalId(bareMetalId);
            CatalogMapper catalogMapper = applicationContext.getBean(CatalogMapper.class);
            CatalogExample ce = new CatalogExample();
            ce.createCriteria().andBareMetalIdEqualTo(bareMetalId).andSourceEqualTo("bmc");
            if (outBand == null) {
                if (catalogMapper.countByExample(ce) > 0) {
                    //has bmc
                    RSException.throwExceptions("no obm info set!");
                } else {
                    //no bmc
                    return;
                }
            }
            IPMIUtil.Account account = IPMIUtil.Account.build(outBand);
            switch (action) {
                case "setBootPxe":
                    IPMIUtil.exeCommand(account, "chassis setbootdev pxe");
                    break;

                case "reboot":
                    String commandResult = IPMIUtil.exeCommand(account, "power status");
                    if (commandResult.contains(RackHDConstants.PM_POWER_ON) || commandResult.contains("On")) {
                        IPMIUtil.exeCommand(account, "chassis setbootdev pxe");
                        IPMIUtil.exeCommand(account, "power reset");
                    } else if (commandResult.contains(RackHDConstants.PM_POWER_OFF) || commandResult.contains("Off")) {
                        IPMIUtil.exeCommand(account, "chassis setbootdev pxe");
                        IPMIUtil.exeCommand(account, "power on");
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
