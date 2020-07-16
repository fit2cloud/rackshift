package io.rackshift.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.model.MachineEntity;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.RackHDService;
import io.rackshift.utils.RackHDHttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class SyncRackJob {
    @Value("rackhd.url:http://127.0.0.1:9090")
    private String rackhdUrl;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private BareMetalService bareMetalService;
    @Value("${run.mode:local}")
    private String runModel;

//    @Scheduled(fixedDelay = 1000)
    @Scheduled(fixedDelay = 60000)
    public void run() {
        List<MachineEntity> entities = new LinkedList<>();
        JSONArray nodesArr = null;
        if ("release".equalsIgnoreCase(runModel)) {
            nodesArr = JSONArray.parseArray(RackHDHttpClientUtil.get(rackhdUrl + RackHDConstants.NODES_URL, null));
        } else {
            nodesArr = rackHDService.getLocalNodes();
        }
        for (int i = 0; i < nodesArr.size(); i++) {
            JSONObject nodeObj = nodesArr.getJSONObject(i);
            String type = nodeObj.getString("type");
            if ("compute".equalsIgnoreCase(type)) {
                MachineEntity en = rackHDService.getNodeEntity(rackhdUrl, nodeObj.getString("id"), null);
                if (en != null) {
                    entities.add(en);
                }
            }
        }

        entities.forEach(e -> {
            bareMetalService.saveOrUpdateEntity(e);
        });
    }


}
