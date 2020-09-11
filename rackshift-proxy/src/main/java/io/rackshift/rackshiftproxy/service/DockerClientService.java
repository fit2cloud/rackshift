package io.rackshift.rackshiftproxy.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import io.rackshift.metal.sdk.util.LogUtil;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DockerClientService {
    private DockerClient client;
    private static final List statusList = Arrays.asList("created", "restarting", "running", "paused", "exited");
    private static final List runningStatusList = Arrays.asList("restarting", "running");

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

}
