package io.rackshift.mybatis.mapper.ext;

import io.rackshift.model.BareMetalRuleDTO;
import io.rackshift.model.BareMetalRuleVO;

import java.util.List;

public interface ExtBareMetalRuleMapper {
    List<BareMetalRuleDTO> list(BareMetalRuleVO queryVO);
}
