package io.rackshift.plugin.h3c;

import com.google.gson.Gson;
import io.rackshift.metal.sdk.IMetalProvider;
import io.rackshift.metal.sdk.model.request.IPMISnmpRequest;
import org.junit.Before;
import org.junit.Test;

public class PluginTest {
    private IPMISnmpRequest request;
    private IMetalProvider iMetalProvider;
    private Gson gson;

    @Before
    public void setP() {
        request = new IPMISnmpRequest("xxx", "admin", "Fit2cloud@2019");
        request.setCommunity("public");
        request.setPort(161);
        gson = new Gson();
    }

    @Test
    public void testGetMachineEntity() {
        System.out.println(gson.toJson(iMetalProvider.getMachineEntity(gson.toJson(request))));
        System.out.println("---haghahahh");
//        System.out.println(gson.toJson(iMetalProvider.logout(gson.toJson(request))));
        System.out.println(gson.toJson(iMetalProvider.getMachineEntity(gson.toJson(request))));
    }

    @Test
    public void testGetMetrics() {
        System.out.println(gson.toJson(iMetalProvider.getMetric(gson.toJson(request))));
    }

    @Test
    public void testHighFrequencySpider() {
        for (int i = 0; i < 20; i++) {
            System.out.println(gson.toJson(iMetalProvider.getMachineEntity(gson.toJson(request))));
        }
    }

}
