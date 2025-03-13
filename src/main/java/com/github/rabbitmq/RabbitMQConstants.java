package com.github.rabbitmq;

public class RabbitMQConstants {

    // Fanout exchange
    public static final String FANOUT_EXCHANGE_NAME = "ex.fanout";
    // Fanout queues
    public static final String FANOUT_QUEUE_1_NAME = "my.queue1";
    public static final String FANOUT_QUEUE_2_NAME = "my.queue2";

    // Direct exchange
    public static final String DIRECT_EXCHANGE_NAME = "ex.direct";
    // Direct queues
    public static final String DIRECT_QUEUE_INFOS_NAME = "my.infos";
    public static final String DIRECT_QUEUE_WARNINGS_NAME = "my.warnings";
    public static final String DIRECT_QUEUE_ERRORS_NAME = "my.errors";
    // Direct routing keys
    public static final String DIRECT_ROUTING_KEY_INFO = "info";
    public static final String DIRECT_ROUTING_KEY_WARNING = "warning";
    public static final String DIRECT_ROUTING_KEY_ERROR = "error";

    private RabbitMQConstants() {

    }
}