package com.sync.counter.client.console.exception;

/**
 * Created by sidnei on 04/02/16.
 */
public class InvalidInputException extends Exception {

    private final String inputLine;

    public InvalidInputException(String inputLine) {
        this.inputLine = inputLine;
    }

    public String getInputLine() {
        return inputLine;
    }
}
