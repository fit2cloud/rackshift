package io.rackshift.plugin.dell.model;

import com.google.gson.annotations.SerializedName;

public class DellPdDTO {

    /**
     * capable_speed : 8
     * commit_job : false
     * data_integrity_field : 1
     * device_description : Disk 0 in Backplane 1 of Integrated RAID Controller 1
     * device_protocol :
     * enclosures : /sysmgmt/2010/storage/enclosure/308|C|Enclosure.Internal.0-1:RAID.Integrated.1-1
     * fail_predict : false
     * firmware_version : TT31
     * form_factor : 2
     * free_space : 0
     * hotspare : 0
     * led : /sysmgmt/2013/storage/led?pdisk_keys=304|C|Disk.Bay.0:Enclosure.Internal.0-1:RAID.Integrated.1-1
     * manufactured_day : 7
     * manufactured_week : 42
     * manufactured_year : 2015
     * manufacturer : SEAGATE
     * media_type : 0
     * model :
     * name : Physical Disk 0:1:0
     * negotiated_speed : 8
     * op_state : 65535
     * part_number : CN0R95FV726225AF02T7A00
     * pcie_capable_link_width : 0
     * pcie_negotiated_link_width : 0
     * pending_changes : 0
     * power_status : 0
     * product_id : ST600MM0088
     * progress : 0
     * protocol : 32
     * remaining_drive_life : 255
     * sasAddress : 5000C5008F45A88D
     * sector_size : 512
     * sedcapability : 0
     * serial_number : S420C295
     * size : 599550590976
     * slot : 0
     * state : 2
     * status : 2
     * used_space : 599550590976
     * vdisks : /sysmgmt/2010/storage/vdisk?pdisk=304|C|Disk.Bay.0:Enclosure.Internal.0-1:RAID.Integrated.1-1
     */

    @SerializedName("capable_speed")
    private int capableSpeed;
    @SerializedName("commit_job")
    private boolean commitJob;
    @SerializedName("data_integrity_field")
    private int dataIntegrityField;
    @SerializedName("device_description")
    private String deviceDescription;
    @SerializedName("device_protocol")
    private String deviceProtocol;
    @SerializedName("enclosures")
    private String enclosures;
    @SerializedName("fail_predict")
    private boolean failPredict;
    @SerializedName("firmware_version")
    private String firmwareVersion;
    @SerializedName("form_factor")
    private int formFactor;
    @SerializedName("free_space")
    private float freeSpace;
    @SerializedName("hotspare")
    private int hotspare;
    @SerializedName("led")
    private String led;
    @SerializedName("manufactured_day")
    private int manufacturedDay;
    @SerializedName("manufactured_week")
    private int manufacturedWeek;
    @SerializedName("manufactured_year")
    private int manufacturedYear;
    @SerializedName("manufacturer")
    private String manufacturer;
    @SerializedName("media_type")
    private int mediaType;
    @SerializedName("model")
    private String model;
    @SerializedName("name")
    private String name;
    @SerializedName("negotiated_speed")
    private int negotiatedSpeed;
    @SerializedName("op_state")
    private int opState;
    @SerializedName("part_number")
    private String partNumber;
    @SerializedName("pcie_capable_link_width")
    private int pcieCapableLinkWidth;
    @SerializedName("pcie_negotiated_link_width")
    private int pcieNegotiatedLinkWidth;
    @SerializedName("pending_changes")
    private int pendingChanges;
    @SerializedName("power_status")
    private int powerStatus;
    @SerializedName("product_id")
    private String productId;
    @SerializedName("progress")
    private int progress;
    @SerializedName("protocol")
    private int protocol;
    @SerializedName("remaining_drive_life")
    private int remainingDriveLife;
    @SerializedName("sasAddress")
    private String sasAddress;
    @SerializedName("sector_size")
    private float sectorSize;
    @SerializedName("sedcapability")
    private int sedcapability;
    @SerializedName("serial_number")
    private String serialNumber;
    @SerializedName("size")
    private float size;
    @SerializedName("slot")
    private int slot;
    @SerializedName("state")
    private int state;
    @SerializedName("status")
    private int status;
    @SerializedName("used_space")
    private float usedSpace;
    @SerializedName("vdisks")
    private String vdisks;

    public int getCapableSpeed() {
        return capableSpeed;
    }

    public void setCapableSpeed(int capableSpeed) {
        this.capableSpeed = capableSpeed;
    }

    public boolean isCommitJob() {
        return commitJob;
    }

    public void setCommitJob(boolean commitJob) {
        this.commitJob = commitJob;
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

    public String getDeviceProtocol() {
        return deviceProtocol;
    }

    public void setDeviceProtocol(String deviceProtocol) {
        this.deviceProtocol = deviceProtocol;
    }

    public String getEnclosures() {
        return enclosures;
    }

    public void setEnclosures(String enclosures) {
        this.enclosures = enclosures;
    }

    public boolean isFailPredict() {
        return failPredict;
    }

    public void setFailPredict(boolean failPredict) {
        this.failPredict = failPredict;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public int getFormFactor() {
        return formFactor;
    }

    public void setFormFactor(int formFactor) {
        this.formFactor = formFactor;
    }

    public float getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(int freeSpace) {
        this.freeSpace = freeSpace;
    }

    public int getHotspare() {
        return hotspare;
    }

    public void setHotspare(int hotspare) {
        this.hotspare = hotspare;
    }

    public String getLed() {
        return led;
    }

    public void setLed(String led) {
        this.led = led;
    }

    public int getManufacturedDay() {
        return manufacturedDay;
    }

    public void setManufacturedDay(int manufacturedDay) {
        this.manufacturedDay = manufacturedDay;
    }

    public int getManufacturedWeek() {
        return manufacturedWeek;
    }

    public void setManufacturedWeek(int manufacturedWeek) {
        this.manufacturedWeek = manufacturedWeek;
    }

    public int getManufacturedYear() {
        return manufacturedYear;
    }

    public void setManufacturedYear(int manufacturedYear) {
        this.manufacturedYear = manufacturedYear;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNegotiatedSpeed() {
        return negotiatedSpeed;
    }

    public void setNegotiatedSpeed(int negotiatedSpeed) {
        this.negotiatedSpeed = negotiatedSpeed;
    }

    public int getOpState() {
        return opState;
    }

    public void setOpState(int opState) {
        this.opState = opState;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public int getPcieCapableLinkWidth() {
        return pcieCapableLinkWidth;
    }

    public void setPcieCapableLinkWidth(int pcieCapableLinkWidth) {
        this.pcieCapableLinkWidth = pcieCapableLinkWidth;
    }

    public int getPcieNegotiatedLinkWidth() {
        return pcieNegotiatedLinkWidth;
    }

    public void setPcieNegotiatedLinkWidth(int pcieNegotiatedLinkWidth) {
        this.pcieNegotiatedLinkWidth = pcieNegotiatedLinkWidth;
    }

    public int getPendingChanges() {
        return pendingChanges;
    }

    public void setPendingChanges(int pendingChanges) {
        this.pendingChanges = pendingChanges;
    }

    public int getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(int powerStatus) {
        this.powerStatus = powerStatus;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public int getRemainingDriveLife() {
        return remainingDriveLife;
    }

    public void setRemainingDriveLife(int remainingDriveLife) {
        this.remainingDriveLife = remainingDriveLife;
    }

    public String getSasAddress() {
        return sasAddress;
    }

    public void setSasAddress(String sasAddress) {
        this.sasAddress = sasAddress;
    }

    public float getSectorSize() {
        return sectorSize;
    }

    public void setSectorSize(float sectorSize) {
        this.sectorSize = sectorSize;
    }

    public int getSedcapability() {
        return sedcapability;
    }

    public void setSedcapability(int sedcapability) {
        this.sedcapability = sedcapability;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
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

    public float getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(float usedSpace) {
        this.usedSpace = usedSpace;
    }

    public String getVdisks() {
        return vdisks;
    }

    public void setVdisks(String vdisks) {
        this.vdisks = vdisks;
    }
}
