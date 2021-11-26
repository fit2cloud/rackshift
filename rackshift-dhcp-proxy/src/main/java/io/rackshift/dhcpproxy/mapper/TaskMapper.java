package io.rackshift.dhcpproxy.mapper;

import org.apache.ibatis.annotations.Param;

public interface TaskMapper {
    String selectActiveGraphObjectsByBMId(@Param("id") String id);
}
