package io.rackshift.controller;

import io.rackshift.mybatis.domain.UserKey;
import io.rackshift.service.UserKeysService;
import io.rackshift.utils.SessionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("user/key")
public class UserKeysController {

    @Resource
    private UserKeysService userKeysService;

    @RequestMapping("info")
    public List<UserKey> getUserKeysInfo() {
        String userId = SessionUtils.getUser().getId();
        return userKeysService.getUserKeysInfo(userId);
    }

    @RequestMapping("generate")
    public void generateUserKey() {
        String userId = SessionUtils.getUser().getId();
        userKeysService.generateUserKey(userId);
    }

    @RequestMapping("delete/{id}")
    public void deleteUserKey(@PathVariable String id) {
        userKeysService.deleteUserKey(id);
    }

    @RequestMapping("active/{id}")
    public void activeUserKey(@PathVariable String id) {
        userKeysService.activeUserKey(id);
    }

    @RequestMapping("disabled/{id}")
    public void disabledUserKey(@PathVariable String id) {
        userKeysService.disabledUserKey(id);
    }
}
