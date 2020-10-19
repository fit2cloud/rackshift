package com.fit2cloud.plugin.hp;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import io.rackshift.metal.sdk.IMetalProvider;
import io.rackshift.metal.sdk.model.request.IPMISnmpRequest;
import io.rackshift.plugin.hp.HpMetalProvider;
import io.rackshift.plugin.hp.model.HpCpuDTO;
import org.junit.Before;
import org.junit.Test;

public class PluginTest {
    private IPMISnmpRequest request;
    private IMetalProvider iMetalProvider;
    private Gson gson;

    @Before
    public void setP() {
        request = new IPMISnmpRequest("10.132.46.250", "administrator", "11111");
        request.setCommunity("public");
        request.setPort(161);
        iMetalProvider = new HpMetalProvider();
        gson = new Gson();
    }

    @Test
    public void testGetMachineEntity() {
        System.out.println(gson.toJson(iMetalProvider.getMachineEntity(gson.toJson(request))));
        System.out.println("-----");
        System.out.println(gson.toJson(iMetalProvider.logout(gson.toJson(request))));
        System.out.println(gson.toJson(iMetalProvider.getMachineEntity(gson.toJson(request))));
    }

    @Test
    public void testGetMetrics() {
        System.out.println(gson.toJson(iMetalProvider.getMetric(gson.toJson(request))));
    }

    @Test
    public void testHighFrequencySpider() {
        for (int i = 0; i < 10; i++) {
            System.out.println(gson.toJson(iMetalProvider.getMachineEntity(gson.toJson(request))));
        }
    }

    @Test
    public void testILO3() {
        String cpuJson = "{\"hostpwr_state\":\"ON\",\"processors\":[{\"proc_socket\":\"Proc 1\",\"proc_speed\":2133,\"proc_num_cores_enabled\":8,\"proc_num_cores\":8,\"proc_num_threads\":16,\"proc_mem_technology\":\"64-bit Capable\",\"proc_num_l1cache\":256,\"proc_num_l2cache\":2048,\"proc_num_l3cache\":24576},{\"proc_socket\":\"Proc 2\",\"proc_speed\":2133,\"proc_num_cores_enabled\":8,\"proc_num_cores\":8,\"proc_num_threads\":16,\"proc_mem_technology\":\"64-bit Capable\",\"proc_num_l1cache\":256,\"proc_num_l2cache\":2048,\"proc_num_l3cache\":24576},{\"proc_socket\":\"Proc 3\",\"proc_speed\":2133,\"proc_num_cores_enabled\":8,\"proc_num_cores\":8,\"proc_num_threads\":16,\"proc_mem_technology\":\"64-bit Capable\",\"proc_num_l1cache\":256,\"proc_num_l2cache\":2048,\"proc_num_l3cache\":24576},{\"proc_socket\":\"Proc 4\",\"proc_speed\":2133,\"proc_num_cores_enabled\":8,\"proc_num_cores\":8,\"proc_num_threads\":16,\"proc_mem_technology\":\"64-bit Capable\",\"proc_num_l1cache\":256,\"proc_num_l2cache\":2048,\"proc_num_l3cache\":24576}]}";
        JSONArray arr = JSONObject.parseObject(cpuJson).getJSONArray("processors");
        HpCpuDTO cpuDTO = gson.fromJson(arr.getJSONObject(0).toJSONString(), HpCpuDTO.class);
        System.out.println(cpuDTO);
    }
}
