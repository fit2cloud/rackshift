package io.rackshift.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.MachineEntity;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
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
    @Resource
    private RackHDService rackHDService;
    @Resource
    private BareMetalManager bareMetalManager;
    @Value("${run.mode:local}")
    private String runModel;
    @Resource
    private SystemParameterMapper systemParameterMapper;

//        @Scheduled(fixedDelay = 1000)
    @Scheduled(fixedDelay = 60000)
    public void run() {
        List<MachineEntity> entities = new LinkedList<>();
        JSONArray nodesArr = null;
        SystemParameter systemParameter = systemParameterMapper.selectByPrimaryKey(ServiceConstants.ENDPOINT_KEY);
        if ("release".equalsIgnoreCase(runModel)) {
            nodesArr = JSONArray.parseArray(RackHDHttpClientUtil.get("http://" + systemParameter.getParamValue() + ":9090" + RackHDConstants.NODES_URL, null));
        } else {
            nodesArr = rackHDService.getLocalNodes();
        }
        for (int i = 0; i < nodesArr.size(); i++) {
            JSONObject nodeObj = nodesArr.getJSONObject(i);
            String type = nodeObj.getString("type");
            if ("compute".equalsIgnoreCase(type)) {
                MachineEntity en = rackHDService.getNodeEntity("http://" + systemParameter.getParamValue() + ":9090", nodeObj.getString("id"), null);
                if (en != null) {
                    entities.add(en);
                }
            }
        }

        entities.forEach(e -> {
            bareMetalManager.saveOrUpdateEntity(e);
        });
    }


}
