package com.sync.counter.common.protocol;

public abstract class Message<E extends Enum, V extends Number> {

    protected final E type;
    protected final V value;

    Message(E type, V value) {
        this.type = type;
        this.value = value;
    }

    public E getType() {
        return type;
    }

    public V getValue() {
        return value;
    }

}
