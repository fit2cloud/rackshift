package io.rackshift.controller;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("test")
public class TestController {
    @Resource
    private Cache cache;

    @RequestMapping("cache")
    public Object getCache() {
        Element element = new Element("123", 1000l);
        cache.put(element);
        return cache.get("123");
    }

}
