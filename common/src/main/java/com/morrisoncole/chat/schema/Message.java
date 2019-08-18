package com.morrisoncole.chat.schema;

public enum Message {
    KIND("message"),
    USER_ID("userId"),
    TIMESTAMP("timestamp"),
    CONTENT("content");

    private final String value;

    Message(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
