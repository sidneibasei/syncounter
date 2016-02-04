package com.sync.counter.client.console.command;

import static com.sync.counter.common.protocol.CounterMessage.MessageType;

/**
 * Created by sidnei on 04/02/16.
 */
public enum CommandType {
    inc("Increments the server counter.", "inc [n]. N is optional. Default 1", MessageType.inc),
    dec("Decrements the server counter.", "dec [n]. N is optional. Default 1", MessageType.dec),
    get("Gets the current counter value", "get", MessageType.get),
    exit("Exit system", "exit", null),
    help("Show this help", "help", null);

    private final String description;
    private final String example;
    private final MessageType messageType;

    CommandType(String description, String example, MessageType messageType) {
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

    public MessageType getMessageType() {
        return messageType;
    }
}
