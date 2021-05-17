package io.rackshift.model;

import io.rackshift.mybatis.domain.BareMetalRule;
import io.rackshift.mybatis.domain.SwitchRule;

public class SwitchRuleDTO extends SwitchRule {

    private String number;
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
