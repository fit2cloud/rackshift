package io.rackshift.engine.job;

import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.OutBandService;
import io.rackshift.utils.IPMIUtil;

@Jobs("Job.Obm.Node")
public class JobObmNode extends BaseJob {
    public JobObmNode() {

    }

    @Override
    public void run() {
        String action = this.options.getString("action");
        try {
            new OBMService(action).run();
            this.complete();
        } catch (Exception e) {
            this.error();
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
            if (outBand == null) {
                RSException.throwExceptions("no obm info set!");
            }
            IPMIUtil.Account account = IPMIUtil.Account.build(outBand);
            switch (action) {
                case "setBootPxe":
                    IPMIUtil.exeCommand(account, "chassis setbootdev pxe");
                    break;

                case "reboot":
                    IPMIUtil.exeCommand(account, "chassis power off");
                    IPMIUtil.exeCommand(account, "chassis power on");
                    break;

                default:
                    break;
            }
        }
    }

    public static void main(String[] args) {
        new JobObmNode().run();
    }
}
