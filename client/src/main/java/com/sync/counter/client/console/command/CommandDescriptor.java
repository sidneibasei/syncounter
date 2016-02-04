package com.sync.counter.client.console.command;

/**
 * Created by sidnei on 04/02/16.
 */
public class CommandDescriptor {

    private final CommandType type;

    private final Integer argument;

    public CommandDescriptor(CommandType type, Integer argument) {
        this.type = type;
        this.argument = argument;
    }

    public CommandType getType() {
        return type;
    }

    public Integer getArgument() {
        return argument;
    }

    public Boolean isExit() {
        return com.sync.counter.client.console.command.CommandType.exit.equals(type);
    }

    @Override
    public String toString() {
        return "CommandDescriptor{" +
                "type=" + type +
                ", argument=" + argument +
                '}';
    }
}
