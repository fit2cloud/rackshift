package io.rackshift.constants;

public class ExecutionLogConstants {
    public static final String SUBMIT = "SUBMIT";
    public static final String PROCESSING = "PROCESSING";
    public static final String END = "END";
    public static final String ERROR = "ERROR";

    public enum OperationEnum {
        START("开始"),
        ERROR("异常"),
        TERMINATE("用户终止"),
        END("结束"),
        ;
        private String name;

        OperationEnum(String name) {
            this.name = name;
        }
    }
}
