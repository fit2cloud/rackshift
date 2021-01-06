package io.rackshift.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.InvocationBuilder;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DockerClientService {
    private DockerClient client;
    private static final List statusList = Arrays.asList("created", "restarting", "running", "paused", "exited");
    private static final List runningStatusList = Arrays.asList("restarting", "running");
    private static final int cmdWaitTime = 10;
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
                try {
                    client.pullImageCmd(s.get("image")).exec(new InvocationBuilder.AsyncResultCallback() {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            String r = String.format("Docker 镜像【%s】在线安装失败！请手动执行 docker load -i 进行加载", s.get("image"));
                            addInstructionLog(instruction.getId(), r);
                        }
                    }).awaitCompletion(cmdWaitTime, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    addInstructionLog(instruction.getId(), String.format("异常 pull image【%s】：", s.get("image")));
                    return false;
                }
            }
            runCmd(instruction, s);

        }
        return true;
    }

    private void runCmd(Instruction instruction, Map<String, String> s) {
        String containerId = client.createContainerCmd(s.get("image")).withCmd(s.get("cmd").split(" ")).exec().getId();
        client.startContainerCmd(containerId).exec();

        LogContainerCmd logContainerCmd = client.logContainerCmd(containerId);
        logContainerCmd.withStdOut(true).withStdErr(true);
        StringBuffer sb = new StringBuffer();

        try {
            logContainerCmd.exec(new ResultCallback.Adapter<Frame>() {
                @Override
                public void onNext(Frame item) {
                    sb.append(item.toString());
                }
            }).awaitCompletion(cmdWaitTime, TimeUnit.MINUTES);
            addInstructionLog(instruction.getId(), sb.toString());
        } catch (InterruptedException e) {
            addInstructionLog(instruction.getId(), "异常：" + e);
        }

        client.removeContainerCmd(containerId);
    }


    private void addInstructionLog(String instructionId, String output) {
        InstructionLog log = new InstructionLog();
        log.setInstructionId(instructionId);
        log.setContent(output);
        instructionLogMapper.insertSelective(log);
    }
}
