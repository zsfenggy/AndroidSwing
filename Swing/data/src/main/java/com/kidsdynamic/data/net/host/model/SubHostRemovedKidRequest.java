package com.kidsdynamic.data.net.host.model;

/**
 * Created by Administrator on 2017/10/21.
 */

public class SubHostRemovedKidRequest {

    /**
     * subHostId : 1
     * kidId : 1
     */

    private int subHostId;
    private int kidId;

    public int getSubHostId() {
        return subHostId;
    }

    public void setSubHostId(int subHostId) {
        this.subHostId = subHostId;
    }

    public int getKidId() {
        return kidId;
    }

    public void setKidId(int kidId) {
        this.kidId = kidId;
    }
}
