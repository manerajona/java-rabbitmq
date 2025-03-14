package com.github.rabbitmq.direct;

class Constants {

    static final String DIRECT_EXCHANGE_NAME = "ex.direct";

    static final String DIRECT_QUEUE_INFOS_NAME = "my.infos";
    static final String DIRECT_QUEUE_WARNINGS_NAME = "my.warnings";
    static final String DIRECT_QUEUE_ERRORS_NAME = "my.errors";

    static final String DIRECT_ROUTING_KEY_INFO = "info";
    static final String DIRECT_ROUTING_KEY_WARNING = "warning";
    static final String DIRECT_ROUTING_KEY_ERROR = "error";

    private Constants() {
    }
}