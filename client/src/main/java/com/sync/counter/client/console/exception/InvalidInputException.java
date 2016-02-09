package com.sync.counter.client.console.exception;

public class InvalidInputException extends Exception {

    private final String inputLine;

    public InvalidInputException(String inputLine) {
        this.inputLine = inputLine;
    }

    public String getInputLine() {
        return inputLine;
    }
}
