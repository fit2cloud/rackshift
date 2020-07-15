package io.rackshift.plugin.dell.model.idrac7;

public class DellIDrac7CpuDTO {

    private String Model;
    private String Manufacturer;
    private String NumberOfProcessorCores;
    private String NumberOfEnabledThreads;
    private String NumberOfEnabledCores;
    private String DeviceDescription;
    private String FQDD;
    private String CPUFamily;
    private String MaxClockSpeed;
    private String CurrentClockSpeed;
    private String Cache3Size;
    private String Cache2Size;
    private String Cache1Size;


    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getNumberOfProcessorCores() {
        return NumberOfProcessorCores;
    }

    public void setNumberOfProcessorCores(String numberOfProcessorCores) {
        NumberOfProcessorCores = numberOfProcessorCores;
    }

    public String getNumberOfEnabledThreads() {
        return NumberOfEnabledThreads;
    }

    public void setNumberOfEnabledThreads(String numberOfEnabledThreads) {
        NumberOfEnabledThreads = numberOfEnabledThreads;
    }

    public String getNumberOfEnabledCores() {
        return NumberOfEnabledCores;
    }

    public void setNumberOfEnabledCores(String numberOfEnabledCores) {
        NumberOfEnabledCores = numberOfEnabledCores;
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

    public String getCPUFamily() {
        return CPUFamily;
    }

    public void setCPUFamily(String CPUFamily) {
        this.CPUFamily = CPUFamily;
    }

    public String getMaxClockSpeed() {
        return MaxClockSpeed;
    }

    public void setMaxClockSpeed(String maxClockSpeed) {
        MaxClockSpeed = maxClockSpeed;
    }

    public String getCurrentClockSpeed() {
        return CurrentClockSpeed;
    }

    public void setCurrentClockSpeed(String currentClockSpeed) {
        CurrentClockSpeed = currentClockSpeed;
    }

    public String getCache3Size() {
        return Cache3Size;
    }

    public void setCache3Size(String cache3Size) {
        Cache3Size = cache3Size;
    }

    public String getCache2Size() {
        return Cache2Size;
    }

    public void setCache2Size(String cache2Size) {
        Cache2Size = cache2Size;
    }

    public String getCache1Size() {
        return Cache1Size;
    }

    public void setCache1Size(String cache1Size) {
        Cache1Size = cache1Size;
    }
}
