package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.BareMetalRuleDTO;
import io.rackshift.model.BareMetalRuleVO;
import io.rackshift.model.SwitchRuleDTO;

import java.util.List;

public interface ExtSwitchRuleMapper {
    List<SwitchRuleDTO> list(SwitchRuleDTO queryVO);
}
