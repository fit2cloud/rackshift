package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.TaskDTO;
import io.rackshift.mybatis.domain.TaskWithBLOBs;

import java.util.List;
import java.util.Map;

public interface ExtTaskMapper {
    List<Map> list(TaskDTO param);
}