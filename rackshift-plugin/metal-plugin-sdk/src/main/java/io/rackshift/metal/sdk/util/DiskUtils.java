package io.rackshift.metal.sdk.util;

import org.apache.commons.lang3.StringUtils;

public class DiskUtils {
    /**
     * 将558等实际值转换为形如600的标称值
     */
    public static String getDiskManufactorValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        String unit = value.contains("GB") ? "GB" : value.contains("TB") ? "TB" : value.contains("G") ? "GB" : value.contains("T") ?
                "TB" : "GB";
        value = value.replace("GB", "").replace("TB", "").replace(" ", "").replace("KB", "");
        if (value.indexOf(".") != -1) {
            double realValue = Double.parseDouble(value);
            int standardValue = (int) (realValue / 0.931);
            if (standardValue % 10 != 0) {
                standardValue += 10 - standardValue % 10;
            }
            return standardValue + " " + unit;
        }

        int tempSize = Integer.valueOf(value);
        if (tempSize % 10 == 0) {
            return value;
        } else {
            int standardValue = (int) (tempSize / 0.931);
            if (standardValue % 10 != 0) {
                standardValue += 10 - standardValue % 10;
            }
            return standardValue + " " + unit;
        }
    }

    public static void main(String[] args) {
        System.out.println(getDiskManufactorValue("465 GB"));
    }
}
