package io.rackshift.constants;

public class MqConstants {

    public static final String USERNAME = "guest";
    public static final String PASSWORD = "guest";
    public static final String URI = "amqp://127.0.0.1";
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

}
