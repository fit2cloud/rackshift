package io.rackshift.metal.sdk.util;

import org.apache.commons.lang3.StringUtils;

public class DiskUtils {

    public static final double BYTE_TO_GB_CONSTANT = 1.0 / (1024 * 1024 * 1024);

    /**
     * 将558等实际值转换为形如600的标称值 转换后为 GB
     */
    public static String getDiskManufactorValue(String value) {

        String kbStr = "KB";
        String mbStr = "MB";
        String gbStr = "GB";
        String tbStr = "TB";

        if (StringUtils.isBlank(value)) {
            return null;
        }
        String unitStr = value.replaceAll("\\d+", "")
                .replaceAll("\\.", "")
                .replaceAll(" ", "");

        // if unitStr isBlank,use default unit "GB"
        Unit unit = Unit.valueOf(StringUtils.isNotBlank(unitStr) ? unitStr : gbStr);

        value = value.replace(kbStr, "")
                .replace(mbStr, "")
                .replace(gbStr, "")
                .replace(tbStr, "")
                .replace(" ", "");

        double realValue = Double.parseDouble(value);
        int standardValue = (int) Math.ceil(realValue / 0.931);

        return standardValue * unit.toGB(unit) + " " + Unit.GB.name();
    }


    private enum Unit {
        /**
         * 数据单位转换
         * B  8 byte
         * KB 1000B
         * MB 1000KB
         * GB 1000MB
         * TB 1000GB
         * PB 1000TB
         * EB 1000PB
         */
        B, KB, MB, GB, TB, PB, EB;

        boolean bigger(Unit unit) {
            return unit.ordinal() - GB.ordinal() > 0;
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
        System.out.println(getDiskManufactorValue((599550590976L * BYTE_TO_GB_CONSTANT) + ""));
    }
}
