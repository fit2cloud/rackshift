package io.rackshift.model;

import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;

import java.util.List;

public class BareMetalDTO extends BareMetal {

    private List<OutBand> outBandList;

    public List<OutBand> getOutBandList() {
        return outBandList;
    }

    public void setOutBandList(List<OutBand> outBandList) {
        this.outBandList = outBandList;
    }
}
