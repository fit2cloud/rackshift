package io.rackshift.utils;

public class ExceptionUtils {
    public static String getExceptionDetail(Exception e) {
        if (e == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] elements = e.getStackTrace();
        sb.append("Exception:");
        sb.append("\n");
        sb.append(e + ":" + e.getMessage());
        for (StackTraceElement element : elements) {
            sb.append(",fileName:" + element.getFileName());
            sb.append(",className:" + element.getClassName());
            sb.append(",methodName:" + element.getMethodName());
            sb.append(",lineNumber:" + element.getLineNumber());
            sb.append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(ExceptionUtils.getExceptionDetail(new RuntimeException("ss")));
    }
}
