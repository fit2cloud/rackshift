package io.rackshift.plugin.dell.model;

import com.google.gson.annotations.SerializedName;

public class DellMemoryDTO {


    /**
     * name : DIMM SLOT A1
     * rank : 2
     * size : 8192
     * speed : 1333
     * state : 2
     * status : 2
     * type : 24
     */

    @SerializedName("name")
    private String name;
    @SerializedName("rank")
    private int rank;
    @SerializedName("size")
    private int size;
    @SerializedName("speed")
    private int speed;
    @SerializedName("state")
    private int state;
    @SerializedName("status")
    private int status;
    @SerializedName("type")
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
