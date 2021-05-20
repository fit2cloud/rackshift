package io.rackshift;

import io.rackshift.security.ApiKeyHandler;
import io.rackshift.utils.CodingUtil;
import io.rackshift.utils.UUIDUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestApi {
    @Test
    public void test1() throws Exception {
        String accessKey = "COO9djfNhvt0FeHy";
        String secretKey = "NjoqodubWXTUuMU4";
        String signature = CodingUtil.aesEncrypt(accessKey + "|" + UUIDUtil.newUUID() + "|" + System.currentTimeMillis(), secretKey, accessKey);
        System.out.println(signature);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("accessKey", accessKey);
        request.addHeader("signature", signature);
        String userId = ApiKeyHandler.getUser(request);
        System.out.println(userId);
    }
}
