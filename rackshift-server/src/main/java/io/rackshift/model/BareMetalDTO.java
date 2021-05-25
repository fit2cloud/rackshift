package io.rackshift.model;

import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;

import java.util.List;
import java.util.Map;

public class BareMetalDTO extends BareMetal {
    private boolean hasChildren = true;
    private Map hardware;

    public Map getHardware() {
        return hardware;
    }

    public void setHardware(Map hardware) {
        this.hardware = hardware;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    private List<OutBand> outBandList;

    public List<OutBand> getOutBandList() {
        return outBandList;
    }

    public void setOutBandList(List<OutBand> outBandList) {
        this.outBandList = outBandList;
    }
}
