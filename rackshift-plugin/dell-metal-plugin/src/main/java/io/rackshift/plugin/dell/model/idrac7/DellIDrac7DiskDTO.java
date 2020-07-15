package io.rackshift.plugin.dell.model.idrac7;

public class DellIDrac7DiskDTO {
    private String SerialNumber;
    private String Manufacturer;
    private String Model;
    private String Slot;
    private String FQDD;
    private String SizeInBytes;
    private String RaidStatus;
    private String SASAddress;

    public String getSASAddress() {
        return SASAddress;
    }

    public void setSASAddress(String SASAddress) {
        this.SASAddress = SASAddress;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getSlot() {
        return Slot;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public String getFQDD() {
        return FQDD;
    }

    public void setFQDD(String FQDD) {
        this.FQDD = FQDD;
    }

    public String getSizeInBytes() {
        return SizeInBytes;
    }

    public void setSizeInBytes(String sizeInBytes) {
        SizeInBytes = sizeInBytes;
    }

    public String getRaidStatus() {
        return RaidStatus;
    }

    public void setRaidStatus(String raidStatus) {
        RaidStatus = raidStatus;
    }
}
