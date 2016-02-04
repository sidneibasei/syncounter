package com.sync.counter.client.console;

import org.springframework.stereotype.Component;

import java.io.Console;

/**
 * Created by sidnei on 04/02/16.
 */
@Component
public class ConsoleManager {

    private Console console = System.console();

    public String readConsole() {
        return console.readLine();
    }

    public void writeToConsole(String str, Object ... objs) {
        console.printf(str, objs);
    }
}
