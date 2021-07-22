package io.rackshift.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.LogContainerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.InvocationBuilder;
import com.google.gson.annotations.Expose;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.mybatis.domain.Instruction;
import io.rackshift.mybatis.domain.InstructionLog;
import io.rackshift.mybatis.domain.Plugin;
import io.rackshift.mybatis.mapper.InstructionLogMapper;
import org.apache.commons.lang3.StringUtils;
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

    public boolean runWithContainer(List<Map<String, String>> buildCommand, Plugin plugin, Instruction instruction) {

        for (Map<String, String> s : buildCommand) {
            List<Image> images = client.listImagesCmd().withImageNameFilter(s.get("image")).exec();
            String r;
            if (images.size() == 0) {
                r = String.format("Docker 镜像【%s】不存在,正在执行在线安装...", s.get("image"));
                addInstructionLog(s.get("bareId"), instruction.getId(), r);
                try {
                    client.pullImageCmd(plugin.getImage()).withTag(plugin.getTag()).exec(new InvocationBuilder.AsyncResultCallback() {
                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            String r = String.format("Docker 镜像【%s】在线安装失败！请手动执行 docker pull %s 在线安装或 docker load -i %s 进行离线安装...", plugin.getImage(), plugin.getImage(), plugin.getImage());
                            addInstructionLog(s.get("bareId"), instruction.getId(), r);
                        }
                    }).awaitCompletion(cmdWaitTime, TimeUnit.MINUTES);
                } catch (InterruptedException e) {
                    addInstructionLog(s.get("bareId"), instruction.getId(), String.format("异常 pull image【%s】：", s.get("image")));
                    return false;
                }
            }
            runCmd(instruction, s);

        }
        return true;
    }

    private void runCmd(Instruction instruction, Map<String, String> s) {
        if (StringUtils.isBlank(s.get("cmd"))) {
            addInstructionLog(s.get("bareId"), instruction.getId(), "指令为空!");
            return;
        }
        String containerId = client.createContainerCmd(s.get("image")).withCmd(s.get("cmd").split(" ")).exec().getId();
        client.startContainerCmd(containerId).exec();

        LogContainerCmd logContainerCmd = client.logContainerCmd(containerId);
        logContainerCmd.withStdOut(true).withStdErr(true);
        StringBuffer sb = new StringBuffer();

        try {
            Thread.sleep(2000);
            ResultCallback.Adapter<Frame> rc = new ResultCallback.Adapter<Frame>() {
                @Override
                public void onNext(Frame item) {
                    sb.append(item.toString()).append("\n");
                }
            };

            logContainerCmd.exec(rc).awaitCompletion(10, TimeUnit.MINUTES);
            addInstructionLog(s.get("bareId"), instruction.getId(), sb.toString());

            client.stopContainerCmd(containerId).exec();
            client.removeContainerCmd(containerId).exec();
        } catch (InterruptedException e) {
            addInstructionLog(s.get("bareId"), instruction.getId(), "异常：" + e);
        } catch (DockerException e) {
            LogUtil.info(String.format("关闭停止容器发生异常,status:%s,%s", e.getHttpStatus(), e.getMessage()));
        }
    }


    private void addInstructionLog(String bareMetalId, String instructionId, String output) {
        InstructionLog log = new InstructionLog();
        log.setInstructionId(instructionId);
        log.setContent(output);
        if (StringUtils.isBlank(output)) {
            log.setContent("There is no result returned... maybe cmd is wrong or etc.");
        }
        log.setBareMetalId(bareMetalId);
        instructionLogMapper.insertSelective(log);
    }

    public CreateContainerResponse createContainer(String image, int innerPort, int exposedPort, List<String> envs) {
        ExposedPort innerExposedPort = ExposedPort.tcp(innerPort);
        Ports ports = new Ports();
        ports.bind(innerExposedPort, Ports.Binding.bindPort(exposedPort));
        CreateContainerResponse r = client.createContainerCmd(image)
                .withHostConfig(new HostConfig().withPortBindings(ports))
                .withEnv(envs)
                .withExposedPorts(innerExposedPort).exec();
        return r;
    }

    public void startContainer(String containerId) {
        client.startContainerCmd(containerId).exec();
    }

    public boolean exist(String containerId) {
        for (Container c : client.listContainersCmd().exec()) {
            if (c.getId() == containerId) {
                return true;
            }
        }
        return false;
    }

    public boolean isRunning(String containerId) {
        if (exist(containerId)) {
            return client.inspectContainerCmd(containerId).exec().getState().getRunning();
        }
        return false;
    }

    public String getExposedPort(String containerId, int novncPort) {
        return (client.inspectContainerCmd(containerId).exec().getHostConfig().getPortBindings().getBindings().get(new ExposedPort(novncPort)))[0].getHostPortSpec();
    }

    public void removeContainer(String containerId) {
        if (exist(containerId) && !client.inspectContainerCmd(containerId).exec().getState().getRunning())
            client.removeContainerCmd(containerId).exec();
    }

    public void stopAndRemoveContainer(String containerId) {
        if (exist(containerId) && client.inspectContainerCmd(containerId).exec().getState().getRunning())
            client.stopContainerCmd(containerId).exec();
        this.removeContainer(containerId);
    }
}
