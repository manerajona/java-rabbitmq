package com.github.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperConfig {

    private static final ObjectMapper mapper = new ObjectMapper();

    private MapperConfig() {
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
