package io.rackshift.service;

import io.rackshift.model.PluginDTO;
import io.rackshift.mybatis.domain.ExecutionLogDetailsExample;
import io.rackshift.mybatis.domain.Plugin;
import io.rackshift.mybatis.domain.PluginExample;
import io.rackshift.mybatis.mapper.ExecutionLogDetailsMapper;
import io.rackshift.mybatis.mapper.PluginMapper;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.utils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PluginService {
    @Resource
    private PluginMapper pluginMapper;
    @Resource
    private ExecutionLogDetailsMapper executionLogDetailsMapper;

    public Object add(PluginDTO queryVO) {
        Plugin task = new Plugin();
        BeanUtils.copyBean(task, queryVO);

        pluginMapper.insertSelective(task);
        return true;
    }

    public Object update(Plugin queryVO) {
        pluginMapper.updateByPrimaryKey(queryVO);
        return true;
    }

    public Object del(String id) {
        Plugin task = pluginMapper.selectByPrimaryKey(id);
        if (task == null) return false;
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<Plugin> list(PluginDTO queryVO) {
        return pluginMapper.selectByExample(new PluginExample());
    }

    private PluginExample buildExample(PluginDTO queryVO) {
        return new PluginExample();
    }

    private Map<String, List<LifeEvent>> groupByBM(List<LifeEvent> events) {
        return events.stream().collect(Collectors.groupingBy(LifeEvent::getBareMetalId));
    }

    public Plugin getById(String taskId) {
        return pluginMapper.selectByPrimaryKey(taskId);
    }

    public Object logs(String id) {
        ExecutionLogDetailsExample e = new ExecutionLogDetailsExample();
        e.createCriteria().andLogIdEqualTo(id);
        e.setOrderByClause("create_time asc");
        return executionLogDetailsMapper.selectByExampleWithBLOBs(e);
    }

    public static void main(String[] args) {
        new ArrayList<>().get(0);
    }
}
