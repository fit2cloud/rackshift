package io.rackshift.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DiskSizeUtils {
    
    /**
     * 根据文件大小转换为B、KB、MB、GB单位字符串显示
     *
     * @param filesize 文件的大小（long型）
     * @return 返回 转换后带有单位的字符串
     */
    public static String getLength(long filesize) throws Exception {

        if (filesize < 0) {
            throw new Exception("传入数据格式有误");
        }

        String strFileSize = null;
        if (filesize < 1024) {
            strFileSize = filesize + "B";
            return strFileSize;
        }

        DecimalFormat df = new DecimalFormat("######0.00");

        if ((filesize >= 1024) && (filesize < 1024 * 1024)) {//KB
            strFileSize = df.format(((double) filesize) / 1024) + "KB";
        } else if ((filesize >= 1024 * 1024) && (filesize < 1024 * 1024 * 1024)) {//MB
            strFileSize = df.format(((double) filesize) / (1024 * 1024)) + "MB";
        } else {//GB
            strFileSize = df.format(((double) filesize) / (1024 * 1024 * 1024)) + "GB";
        }
        return strFileSize;
    }

    public static BigDecimal getSize(long filesize, Unit unit) throws Exception {
        return getSize(getLength(filesize), unit);
    }

    /**
     * 根据B、KB、MB、GB单位字符串转化为某个单位的long
     *
     * @param unit 单位
     * @return 返回 转换后的long
     */
    public static BigDecimal getSize(String filesize, Unit unit) throws Exception {
        long nowNumber = 0;
        BigDecimal result;
        int nowUnit = -1;

        filesize = filesize.replaceAll("\\s*", "");
        String unitStr = filesize.replaceAll("\\d+", "");

        for (Unit e : Unit.values()) {
            if (unitStr.equalsIgnoreCase(e.name())) {
                nowUnit = e.getI();
                nowNumber = Long.parseLong(filesize.replace(unitStr, ""));
            }
        }
        if (StringUtils.isBlank(unitStr)) {
            nowUnit = Unit.B.getI();
            nowNumber = Long.parseLong(filesize.replace(unitStr, ""));
        }

        if (nowUnit == -1) {
            throw new Exception("传入数据格式有误");
        }

        int flag = nowUnit - unit.getI();
        result = new BigDecimal(nowNumber);
        if (flag == 0) {
        } else if (flag < 0) {
            for (int i = 0; i < -flag; i++) {
                result = result.divide(new BigDecimal("1024"));
            }
        } else {
            for (int i = 0; i < flag; i++) {
                result = result.multiply(new BigDecimal("1024"));
            }
        }

        return result;
    }

    public enum Unit {
        B(0), KB(1), MB(2), GB(3), TB(4);

        Unit(int i) {
            this.i = i;
        }

        private int i;

        public int getI() {
            return i;
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getSize("8192", Unit.TB).toString());
    }

}