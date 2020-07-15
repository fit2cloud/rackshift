package io.rackshift.plugin.dell.model;

import com.google.gson.annotations.SerializedName;

public class DellVdDTO {

    /**
     * available_cache_policy : 268437367
     * badblocks_found : false
     * controllers : /sysmgmt/2010/storage/controller/301|C|RAID.Integrated.1-1
     * data_integrity_field : 0
     * device_description : Virtual Disk 0 on Integrated RAID Controller 1
     * disk_cache_policy : 256
     * led : /sysmgmt/2013/storage/led?vdisk_keys=305|C|Disk.Virtual.0:RAID.Integrated.1-1
     * media_type : 8
     * name : Virtual Disk 0
     * op_state : 65535
     * pdisks : /sysmgmt/2010/storage/pdisk?vdisk=305|C|Disk.Virtual.0:RAID.Integrated.1-1
     * pending_changes : 0
     * pending_operations : 0
     * pending_status : 0
     * progress : 0
     * protocol : 32
     * raidlevel : 4
     * read_policy : 32
     * remaining_redundancy : 1
     * secondary_cache : false
     * sector_size : 512
     * secured : false
     * size : 599550590976
     * span_count : 1
     * state : 2
     * status : 2
     * stripe_size : 128
     * write_policy : 2
     */

    @SerializedName("available_cache_policy")
    private int availableCachePolicy;
    @SerializedName("badblocks_found")
    private boolean badblocksFound;
    @SerializedName("controllers")
    private String controllers;
    @SerializedName("data_integrity_field")
    private int dataIntegrityField;
    @SerializedName("device_description")
    private String deviceDescription;
    @SerializedName("disk_cache_policy")
    private int diskCachePolicy;
    @SerializedName("led")
    private String led;
    @SerializedName("media_type")
    private int mediaType;
    @SerializedName("name")
    private String name;
    @SerializedName("op_state")
    private int opState;
    @SerializedName("pdisks")
    private String pdisks;
    @SerializedName("pending_changes")
    private int pendingChanges;
    @SerializedName("pending_operations")
    private int pendingOperations;
    @SerializedName("pending_status")
    private int pendingStatus;
    @SerializedName("progress")
    private int progress;
    @SerializedName("protocol")
    private int protocol;
    @SerializedName("raidlevel")
    private int raidlevel;
    @SerializedName("read_policy")
    private int readPolicy;
    @SerializedName("remaining_redundancy")
    private int remainingRedundancy;
    @SerializedName("secondary_cache")
    private boolean secondaryCache;
    @SerializedName("sector_size")
    private int sectorSize;
    @SerializedName("secured")
    private boolean secured;
    @SerializedName("size")
    private long size;
    @SerializedName("span_count")
    private int spanCount;
    @SerializedName("state")
    private int state;
    @SerializedName("status")
    private int status;
    @SerializedName("stripe_size")
    private int stripeSize;
    @SerializedName("write_policy")
    private int writePolicy;

    public int getAvailableCachePolicy() {
        return availableCachePolicy;
    }

    public void setAvailableCachePolicy(int availableCachePolicy) {
        this.availableCachePolicy = availableCachePolicy;
    }

    public boolean isBadblocksFound() {
        return badblocksFound;
    }

    public void setBadblocksFound(boolean badblocksFound) {
        this.badblocksFound = badblocksFound;
    }

    public String getControllers() {
        return controllers;
    }

    public void setControllers(String controllers) {
        this.controllers = controllers;
    }

    public int getDataIntegrityField() {
        return dataIntegrityField;
    }

    public void setDataIntegrityField(int dataIntegrityField) {
        this.dataIntegrityField = dataIntegrityField;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public int getDiskCachePolicy() {
        return diskCachePolicy;
    }

    public void setDiskCachePolicy(int diskCachePolicy) {
        this.diskCachePolicy = diskCachePolicy;
    }

    public String getLed() {
        return led;
    }

    public void setLed(String led) {
        this.led = led;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpState() {
        return opState;
    }

    public void setOpState(int opState) {
        this.opState = opState;
    }

    public String getPdisks() {
        return pdisks;
    }

    public void setPdisks(String pdisks) {
        this.pdisks = pdisks;
    }

    public int getPendingChanges() {
        return pendingChanges;
    }

    public void setPendingChanges(int pendingChanges) {
        this.pendingChanges = pendingChanges;
    }

    public int getPendingOperations() {
        return pendingOperations;
    }

    public void setPendingOperations(int pendingOperations) {
        this.pendingOperations = pendingOperations;
    }

    public int getPendingStatus() {
        return pendingStatus;
    }

    public void setPendingStatus(int pendingStatus) {
        this.pendingStatus = pendingStatus;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public int getRaidlevel() {
        return raidlevel;
    }

    public void setRaidlevel(int raidlevel) {
        this.raidlevel = raidlevel;
    }

    public int getReadPolicy() {
        return readPolicy;
    }

    public void setReadPolicy(int readPolicy) {
        this.readPolicy = readPolicy;
    }

    public int getRemainingRedundancy() {
        return remainingRedundancy;
    }

    public void setRemainingRedundancy(int remainingRedundancy) {
        this.remainingRedundancy = remainingRedundancy;
    }

    public boolean isSecondaryCache() {
        return secondaryCache;
    }

    public void setSecondaryCache(boolean secondaryCache) {
        this.secondaryCache = secondaryCache;
    }

    public int getSectorSize() {
        return sectorSize;
    }

    public void setSectorSize(int sectorSize) {
        this.sectorSize = sectorSize;
    }

    public boolean isSecured() {
        return secured;
    }

    public void setSecured(boolean secured) {
        this.secured = secured;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
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

    public int getStripeSize() {
        return stripeSize;
    }

    public void setStripeSize(int stripeSize) {
        this.stripeSize = stripeSize;
    }

    public int getWritePolicy() {
        return writePolicy;
    }

    public void setWritePolicy(int writePolicy) {
        this.writePolicy = writePolicy;
    }
}
