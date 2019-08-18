package com.morrisoncole.chat.login.schema;

public enum User {
    KIND("user"),
    ID("id");

    private final String value;

    User(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
