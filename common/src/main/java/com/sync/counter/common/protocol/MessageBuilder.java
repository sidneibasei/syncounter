package com.sync.counter.common.protocol;


public abstract class MessageBuilder<E extends Enum, V extends Number, R extends Object> {

    protected E type;
    protected V value;

    public <T extends MessageBuilder<E, V, R>> T withValue(V value) {
        if(value == null) {
            throw new IllegalArgumentException("Value is null");
        }
        this.value = value;
        return (T)this;
    }

    public <T extends MessageBuilder<E, V, R>> T withType(E type) {
        if(type == null) {
            throw new IllegalArgumentException("Type is null");
        }
        this.type = type;
        return (T)this;
    }

    public abstract R build();

}
