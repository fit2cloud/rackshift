package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.model.BareMetalDTO;
import io.rackshift.model.BareMetalQueryVO;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.BareMetalService;
import io.rackshift.utils.PageUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("bare-metal")
public class BareMetalController {
    @Resource
    private BareMetalService bareMetalService;

    @RequestMapping("/list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody BareMetalQueryVO queryVO) {
        Page<BareMetal> pager = PageHelper.startPage(page, pageSize);
        return ResultHolder.success(PageUtils.setPageInfo(pager, bareMetalService.list(queryVO)));
    }

    @RequestMapping("/{id}/{power}")
    public ResultHolder power(@PathVariable String id, @PathVariable String power) {
        return bareMetalService.power(id, power);
    }

    @RequestMapping("/hardwares/{bareId}")
    public ResultHolder hardwares(@PathVariable String bareId) {
        return bareMetalService.hardwares(bareId);
    }
}
