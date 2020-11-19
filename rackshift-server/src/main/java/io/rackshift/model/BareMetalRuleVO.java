package io.rackshift.model;

import io.rackshift.mybatis.domain.BareMetalRule;

public class BareMetalRuleVO extends BareMetalRule {
    private String searchKey;
    private String sort;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
