package com.github.rabbitmq.topic;

class Constants {

    static final String TOPIC_EXCHANGE_NAME = "ex.topic";

    static final String TOPIC_QUEUE_IMG_1_NAME = "my.img1";
    static final String TOPIC_QUEUE_IMG_2_NAME = "my.img2";
    static final String TOPIC_QUEUE_IMG_3_NAME = "my.img3";

    static final String TOPIC_BINDING_KEY_IMG_1 = "*.image.*";
    static final String TOPIC_BINDING_KEY_IMG_2 = "#.image";
    static final String TOPIC_BINDING_KEY_IMG_3 = "image.#";

    static final String TOPIC_ROUTING_KEY_1 = "convert.image.bmp";
    static final String TOPIC_ROUTING_KEY_2 = "convert.bitmap.image";
    static final String TOPIC_ROUTING_KEY_3 = "image.bitmap.32bit";

    private Constants() {
    }
}