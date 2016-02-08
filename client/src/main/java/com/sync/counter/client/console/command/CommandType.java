package com.sync.counter.client.console.command;

import com.sync.counter.common.protocol.CounterMessageRequest.RequestType;

/**
 * Created by sidnei on 04/02/16.
 */
public enum CommandType {
    inc("Increments the server counter.", "inc [n]. N is optional. Default 1", RequestType.inc),
    dec("Decrements the server counter.", "dec [n]. N is optional. Default 1", RequestType.dec),
    get("Gets the current counter value", "get", RequestType.get),
    exit("Exit system", "exit", null),
    help("Show this help", "help", null);

    private final String description;
    private final String example;
    private final RequestType messageType;

    CommandType(String description, String example, RequestType messageType) {
        this.description = description;
        this.example = example;
        this.messageType = messageType;
    }

    public String getDescription() {
        return description;
    }

    public String getExample() {
        return example;
    }

    public RequestType getRequestType() {
        return messageType;
    }
}
