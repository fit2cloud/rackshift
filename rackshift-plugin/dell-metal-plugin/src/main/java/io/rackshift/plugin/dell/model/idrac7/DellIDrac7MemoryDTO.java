package io.rackshift.plugin.dell.model.idrac7;

public class DellIDrac7MemoryDTO {
    private String Model;
    private String PartNumber;
    private String SerialNumber;
    private String Manufacturer;
    private String BankLabel;
    private String DeviceDescription;
    private String FQDD;
    private String Speed;
    private String Size;
    private String MemoryType;

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getPartNumber() {
        return PartNumber;
    }

    public void setPartNumber(String partNumber) {
        PartNumber = partNumber;
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

    public String getBankLabel() {
        return BankLabel;
    }

    public void setBankLabel(String bankLabel) {
        BankLabel = bankLabel;
    }

    public String getDeviceDescription() {
        return DeviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        DeviceDescription = deviceDescription;
    }

    public String getFQDD() {
        return FQDD;
    }

    public void setFQDD(String FQDD) {
        this.FQDD = FQDD;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getMemoryType() {
        return MemoryType;
    }

    public void setMemoryType(String memoryType) {
        MemoryType = memoryType;
    }
}
