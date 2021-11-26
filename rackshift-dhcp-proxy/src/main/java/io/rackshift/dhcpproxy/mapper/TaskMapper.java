package io.rackshift.dhcpproxy.mapper;

import org.apache.ibatis.annotations.Select;

public interface TaskMapper {
    @Select("select graph_objects from task where bare_metal_id = #{id} and status = 'running'")
    String selectActiveGraphObjectsByBMId(String id);
}
