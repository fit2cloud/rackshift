package io.rackshift.metal.sdk.util;

import org.apache.commons.lang3.StringUtils;

public class DiskUtils {
    /**
     * 将558等实际值转换为形如600的标称值 转换后为 GB
     */
    public static String getDiskManufactorValue(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        Unit unit = Unit.valueOf(value.replaceAll("\\d+", "").replaceAll("\\.", "").replaceAll(" ", ""));
        value = value.replace("GB", "").replace("TB", "").replace(" ", "").replace("KB", "");
        double realValue = Double.parseDouble(value);
        int standardValue = (int) Math.ceil(realValue / 0.931);

        return standardValue * unit.toGB(unit) + " " + Unit.GB.name();
    }

    private enum Unit {
        B, MB, GB, TB, PB, EB;

        boolean bigger(Unit unit) {
            return unit.ordinal() - GB.ordinal() > 0 ? true : false;
        }

        int toGB(Unit unit) {
            int pos = Math.abs(unit.ordinal() - GB.ordinal());
            if (bigger(unit)) {
                return pos * 1000;
            } else if (unit.ordinal() == GB.ordinal()) {
                return 1;
            }
            return 1 / (1000) ^ pos;
        }
    }

    public static void main(String[] args) {
        System.out.println(getDiskManufactorValue("1.818 TB"));
    }
}
