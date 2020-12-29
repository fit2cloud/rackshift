package io.rackshift.model;

import io.rackshift.mybatis.domain.OutBand;

public class OutBandDTO extends OutBand {

    private String[] ids;

    private OutBand obm;

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public OutBand getObm() {
        return obm;
    }

    public void setObm(OutBand obm) {
        this.obm = obm;
    }
}
