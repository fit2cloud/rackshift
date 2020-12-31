package io.rackshift.service;

import io.rackshift.model.InstructionDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.InstructionLogMapper;
import io.rackshift.mybatis.mapper.InstructionMapper;
import io.rackshift.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class InstructionService {
    @Resource
    private InstructionMapper instructionMapper;
    @Resource
    private InstructionLogMapper instructionLogMapper;
    @Resource
    private PluginService pluginService;
    @Resource
    private OutBandService outBandService;
    @Resource
    private DockerClientService dockerClientService;

    public Object add(InstructionDTO queryVO) {
        Instruction task = new Instruction();
        BeanUtils.copyBean(task, queryVO);

        instructionMapper.insertSelective(task);
        return true;
    }

    public Object update(Instruction queryVO) {
        instructionMapper.updateByPrimaryKeyWithBLOBs(queryVO);
        return true;
    }

    public Object del(String id) {
        Instruction task = instructionMapper.selectByPrimaryKey(id);
        if (task == null) return false;
        instructionMapper.deleteByPrimaryKey(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<Instruction> list(InstructionDTO queryVO) {
        return instructionMapper.selectByExampleWithBLOBs(buildExample(queryVO));
    }

    private InstructionExample buildExample(InstructionDTO queryVO) {
        return new InstructionExample();
    }


    public Instruction getById(String taskId) {
        return instructionMapper.selectByPrimaryKey(taskId);
    }

    public Object logs(String id) {
        InstructionLogExample e = new InstructionLogExample();
        e.createCriteria().andInstructionIdEqualTo(id);
        e.setOrderByClause("create_time asc");
        return instructionLogMapper.selectByExampleWithBLOBs(e);
    }

    public static void main(String[] args) {
        new ArrayList<>().get(0);
    }

    public boolean runCommands(InstructionDTO instructionDTO) {
        if (StringUtils.isBlank(instructionDTO.getId()) || instructionDTO.getBareMetalIds().length == 0) {
            return false;
        }
        Instruction instruction = instructionMapper.selectByPrimaryKey(instructionDTO.getId());
        if (instruction == null) {
            return false;
        }

        Plugin plugin = pluginService.getById(instruction.getPluginId());

        if (plugin == null) {
            return false;
        }

        List<OutBand> outBands = outBandService.getByBareMetalIds(instructionDTO.getBareMetalIds());

        if (outBands.size() == 0) {
            return false;
        }

        outBands.forEach(o -> {
            dockerClientService.runWithContainer(buildCommand(o, plugin, instruction), instruction);
        });

        return true;
    }

    private List<String> buildCommand(OutBand o, Plugin plugin, Instruction instruction) {
        List<String> commands = new LinkedList<>();
        for (String s : instruction.getContent().split("\n")) {
            commands.add(String.format(plugin.getBaseInstruction().trim() + " ", o.getIp(), o.getUserName(), o.getPwd()) + " " + s);
        }
        return commands;
    }
}
