package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.SwitchPortDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtSwitchPortMapper {
    List<SwitchPortDTO> getPortsById(@Param("id") String id);
}
