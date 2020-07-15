package io.rackshift.plugin.dell.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "root")
public class DellPowerDTO {

    private Powermonitordata powermonitordata;

    public Powermonitordata getPowermonitordata() {
        return powermonitordata;
    }

    public void setPowermonitordata(Powermonitordata powermonitordata) {
        this.powermonitordata = powermonitordata;
    }

    @XmlRootElement(name = "powermonitordata")
    public static class Powermonitordata {
        private PresentReading presentReading;

        public PresentReading getPresentReading() {
            return presentReading;
        }

        public void setPresentReading(PresentReading presentReading) {
            this.presentReading = presentReading;
        }
    }

    @XmlRootElement(name = "presentReading")
    public static class PresentReading {
        private Reading reading;

        public Reading getReading() {
            return reading;
        }

        public void setReading(Reading reading) {
            this.reading = reading;
        }
    }

    @XmlRootElement(name = "reading")
    public static class Reading {
        private String reading;
        private String warningThreshold;
        private String failureThreshold;

        public String getReading() {
            return reading;
        }

        public String getWarningThreshold() {
            return warningThreshold;
        }

        public String getFailureThreshold() {
            return failureThreshold;
        }

        public void setReading(String reading) {
            this.reading = reading;
        }

        public void setWarningThreshold(String warningThreshold) {
            this.warningThreshold = warningThreshold;
        }

        public void setFailureThreshold(String failureThreshold) {
            this.failureThreshold = failureThreshold;
        }
    }
}
