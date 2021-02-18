package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.InstructionDTO;
import io.rackshift.model.UserDTO;

import java.util.List;
import java.util.Map;

public interface ExtInstructionLogMapper {
    List<InstructionDTO> list(Map params);
}
