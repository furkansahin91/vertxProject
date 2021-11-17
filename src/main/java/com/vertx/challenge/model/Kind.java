package com.vertx.challenge.model;


import lombok.Data;

public enum Kind {
    ORPHANED("orphaned"), JOINED("joined");

    private final String val;

    Kind(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
