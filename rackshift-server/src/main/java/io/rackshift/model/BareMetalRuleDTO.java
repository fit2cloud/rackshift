package io.rackshift.model;

import io.rackshift.mybatis.domain.BareMetalRule;

public class BareMetalRuleDTO extends BareMetalRule {

    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
