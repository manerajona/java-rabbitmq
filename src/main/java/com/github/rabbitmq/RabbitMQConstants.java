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

    // Topic exchange
    public static final String TOPIC_EXCHANGE_NAME = "ex.topic";
    // Topic queues
    public static final String TOPIC_QUEUE_IMG_1_NAME = "my.img1";
    public static final String TOPIC_QUEUE_IMG_2_NAME = "my.img2";
    public static final String TOPIC_QUEUE_IMG_3_NAME = "my.img3";
    // Topic binding keys
    public static final String TOPIC_BINDING_KEY_IMG_1 = "*.image.*";
    public static final String TOPIC_BINDING_KEY_IMG_2 = "#.image";
    public static final String TOPIC_BINDING_KEY_IMG_3 = "image.#";
    // Topic routing keys
    public static final String TOPIC_ROUTING_KEY_1 = "convert.image.bmp";
    public static final String TOPIC_ROUTING_KEY_2 = "convert.bitmap.image";
    public static final String TOPIC_ROUTING_KEY_3 = "image.bitmap.32bit";

    private RabbitMQConstants() {

    }
}