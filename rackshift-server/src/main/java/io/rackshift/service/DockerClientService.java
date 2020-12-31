package io.rackshift.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.mybatis.domain.Instruction;
import io.rackshift.mybatis.domain.InstructionLog;
import io.rackshift.mybatis.mapper.InstructionLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DockerClientService {
    private DockerClient client;
    private static final List statusList = Arrays.asList("created", "restarting", "running", "paused", "exited");
    private static final List runningStatusList = Arrays.asList("restarting", "running");

    @Resource
    InstructionLogMapper instructionLogMapper;

    @PostConstruct
    private void init() {
        try {
            client = DockerClientBuilder.getInstance().build();
        } catch (Exception e) {
            throw new RuntimeException("连接docker socket失败!请检查！");
        }
    }

    public boolean restartContainer(String imageName) {
        List<Container> containerList = client.listContainersCmd().withStatusFilter(statusList).exec()
                .stream()
                .filter(m -> m.getImage().contains(imageName))
                .collect(Collectors.toList());
        if (containerList.size() > 0) {
            Container container = containerList.get(0);
            try {
                client.restartContainerCmd(container.getId()).exec();

                containerList = client.listContainersCmd().withStatusFilter(runningStatusList).exec()
                        .stream()
                        .filter(m -> m.getImage().contains(imageName))
                        .collect(Collectors.toList());
                if (containerList.size() > 0) return true;
            } catch (Exception e) {
                LogUtil.error(String.format("重启容器%s失败", container.getId() + "--" + container.getImage()));
            }
        }
        return false;

    }

    public boolean runWithContainer(List<String> buildCommand, Instruction instruction) {

        for (String s : buildCommand) {
            CommitCmd cmd = client.commitCmd(s);
            String r = null;
            try {
                r = cmd.exec();
            } catch (Exception e) {
                r = "执行出错!" + e;
            }
            InstructionLog log = new InstructionLog();
            log.setInstructionId(instruction.getId());
            log.setContent(r);
            instructionLogMapper.insertSelective(log);
        }
        return true;
    }
}
