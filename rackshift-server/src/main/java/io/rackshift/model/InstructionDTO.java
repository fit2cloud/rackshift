package io.rackshift.model;

import io.rackshift.mybatis.domain.Instruction;

public class InstructionDTO extends Instruction {

    private String searchKey;
    private String sort;

    private String[] bareMetalIds;

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

    public String[] getBareMetalIds() {
        return bareMetalIds;
    }

    public void setBareMetalIds(String[] bareMetalIds) {
        this.bareMetalIds = bareMetalIds;
    }
}
