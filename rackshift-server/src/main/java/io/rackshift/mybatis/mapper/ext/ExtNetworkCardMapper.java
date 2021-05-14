package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.NetworkCardDTO;
import io.rackshift.model.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ExtNetworkCardMapper {
    List<NetworkCardDTO> getNicsById(@Param("id") String id);
}
