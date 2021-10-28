package io.rackshift.constants;

public class MqConstants {

    public static final String USERNAME = "guest";
    public static final String PASSWORD = "guest";
    public static final String VIRTUALHOST = "/";

    public static final String EXCHANGE_NAME = "rackshift.exchange.default";
    public static final String RUN_TASKGRAPH_QUEUE_NAME = "rackshift.queue.run.taskgraph";
    public static final String CANCEL_TASKGRAPH_QUEUE_NAME = "rackshift.queue.cancel.taskgraph";
    public static final String RUN_TASK_QUEUE_NAME = "rackshift.queue.run.task";
    public static final String CANCEL_TASK_QUEUE_NAME = "rackshift.queue.cancel.task";


    public static final String ANY = "#";
    public static final String RUN_TASKGRAPH_ROUTINGKEY = "run.taskgraph.";
    public static final String CANCEL_TASKGRAPH_ROUTINGKEY = "cancel.taskgraph.";

    public static final String RUN_TASK_ROUTINGKEY = "run.task.";
    public static final String CANCEL_TASK_ROUTINGKEY = "cancel.task.";

    //for task detail

    public static final String MQ_ROUTINGKEY_COMMANDS= "methods.requestCommands.";
    public static final String MQ_ROUTINGKEY_PROFILES= "methods.requestProfile.";
    public static final String MQ_ROUTINGKEY_OPTIONS= "methods.requestOptions.";
    public static final String MQ_ROUTINGKEY_COMPLETE= "methods.completeCommands.";
    public static final String MQ_ROUTINGKEY_NOTIFICATION= "methods.notification.";
}
