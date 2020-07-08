package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.UserDTO;

import java.util.List;
import java.util.Map;

public interface ExtUserMapper {
    List<UserDTO> list(Map params);
}
