package io.rackshift.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.SyncDockerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
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
import java.util.Map;
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

    public boolean runWithContainer(List<Map<String, String>> buildCommand, Instruction instruction) {

        for (Map<String, String> s : buildCommand) {
            List<Image> images = client.listImagesCmd().withImageNameFilter(s.get("image")).exec();
            String r;
            if (images.size() == 0) {
                r = String.format("Docker 镜像【%s】不存在,正在执行在线安装...", s.get("image"));
                addInstructionLog(instruction.getId(), r);

                client.pullImageCmd(s.get("image")).exec(new ResultCallbackTemplate() {
                    @Override
                    public void onNext(Object o) {
                        SyncDockerCmd cmd = client.createContainerCmd(s.get("image")).withCmd(s.get("cmd"));
                        String r = null;
                        try {
                            r = (String) cmd.exec();
                        } catch (Exception e) {
                            r = "执行出错!" + e;
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        String r = String.format("Docker 镜像【%s】在线安装失败！请手动执行 docker load -i 进行加载", s.get("image"));
                        addInstructionLog(instruction.getId(), r);
                    }
                });
            } else {
                SyncDockerCmd cmd = client.createContainerCmd(s.get("image")).withCmd(s.get("cmd"));
                try {
                    r = (String) cmd.exec();
                } catch (Exception e) {
                    r = "执行出错!" + e;
                }
            }
            addInstructionLog(instruction.getId(), r);
        }
        return true;
    }


    private void addInstructionLog(String instructionId, String output) {
        InstructionLog log = new InstructionLog();
        log.setInstructionId(instructionId);
        log.setContent(output);
        instructionLogMapper.insertSelective(log);
    }
}
