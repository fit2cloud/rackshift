package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.engine.job.BaseJob;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.service.RackHDService;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.*;
import io.rackshift.strategy.statemachine.handler.param.AbstractParamHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

@EventHandlerAnnotation(LifeEventType.POST_OS_WORKFLOW_START)
public class OsWorkflowStartHandler extends AbstractHandler {

    @Resource
    private RackHDService rackHDService;
    @Resource
    private TaskService taskService;
    @Resource
    private AbstractParamHandler abstractParamHandler;
    @Resource
    private Map<String, Class> job;

    @Override
    public void handleYourself(LifeEvent event) {
        String taskId = event.getWorkflowRequestDTO().getTaskId();
        TaskWithBLOBs task = taskService.getById(taskId);

        //下发装机workflow
        WorkflowRequestDTO requestDTO = event.getWorkflowRequestDTO();
        JSONObject params = requestDTO.getParams();
        JSONObject extraParams = requestDTO.getExtraParams();
        BareMetal bareMetal = getBareMetalById(requestDTO.getBareMetalId());

        if (params == null) {
            revert(event);
        }
        if (extraParams != null) {
            if (extraParams.containsKey("unit") && "GB".equalsIgnoreCase(extraParams.getString("unit"))) {
                setPartitionSize(params);
            }

            if (!extraParams.containsKey("customPartition") || (extraParams.containsKey("customPartition") && !extraParams.getBoolean("customPartition"))) {
                removePartitions(params);
            }
        }

        customizeParams(requestDTO.getWorkflowName(), params);

        startTask(task);
        task.setStatus(ServiceConstants.TaskStatusEnum.running.name());
        taskService.update(task);
        changeStatus(event, LifeStatus.deploying, true);
    }

    private void startTask(TaskWithBLOBs task) {
        JSONArray taskArr = JSONArray.parseArray(task.getGraphObjects());
        JSONObject obj = (JSONObject) taskArr.stream().filter(t -> ((JSONObject) t).getJSONObject("waitingOn") == null).findFirst().get();
        String runJob = obj.getString("runJob");
        if (StringUtils.isNotBlank(runJob)) {
            Class c1 = job.get(runJob);
            if(c1 != null){
                try {
                    Constructor c = c1.getConstructor(String.class,String.class,JSONObject.class, TaskMapper.class, ApplicationContext.class);
                    if(c != null){
                       BaseJob bj = (BaseJob) c.newInstance(task.getId());
                       bj.run();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void customizeParams(String injectableName, JSONObject params) {
        if (abstractParamHandler.getHandler(injectableName) != null) {
            abstractParamHandler.getHandler(injectableName).process(params);
        }
    }

    private void removePartitions(JSONObject params) {
        params.getJSONObject("options").getJSONObject("defaults").remove("installPartitions");
    }

    private void setPartitionSize(JSONObject params) {
        JSONObject options = params.getJSONObject("options");
        JSONObject defaults = options.getJSONObject("defaults");
        JSONArray partitions = defaults.getJSONArray("installPartitions");
        for (int i = 0; i < partitions.size(); i++) {
            JSONObject p = partitions.getJSONObject(i);
            if (!p.getString("size").equalsIgnoreCase("auto") && !"biosboot".equalsIgnoreCase(p.getString("mountPoint"))) {
                p.put("size", Integer.valueOf(((JSONObject) p).getString("size")) * 1024 + "");
            }
            partitions.set(i, p);
        }
        defaults.put("installPartitions", partitions);
        options.put("defaults", defaults);
        params.put("options", options);
    }

}
