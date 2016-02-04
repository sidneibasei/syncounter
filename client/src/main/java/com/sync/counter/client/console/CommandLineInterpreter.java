package com.sync.counter.client.console;

import com.sync.counter.client.console.command.CommandDescriptor;
import com.sync.counter.client.console.command.CommandParser;
import com.sync.counter.client.console.command.CommandType;
import com.sync.counter.client.console.exception.InvalidInputException;
import com.sync.counter.client.protocol.SyncClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sidnei on 04/02/16.
 */
@Component
public class CommandLineInterpreter {

    @Autowired
    private ConsoleManager consoleManager;

    @Autowired
    private SyncClient client;

    public void run() {
        CommandDescriptor command = null;
        do {
            consoleManager.writeToConsole("$ ");
            try {
                command = new CommandParser().parse(consoleManager.readConsole());

                switch(command.getType()) {
                    case inc:
                    case dec:
                    case get:
                        proccessRequest(command);
                        break;
                    case help:
                        printHelp();
                        break;
                    case exit:
                        exitSystem();
                        break;
                    
                }
            } catch(InvalidInputException ex) {
                consoleManager.writeToConsole("Invalid input: %s", ex.getInputLine());
                printHelp();

            }
            consoleManager.writeToConsole("\ndone ;)\n");
            consoleManager.writeToConsole("What's next?\n");
        } while(command != null && !command.isExit());
    }

    private void exitSystem() {
        consoleManager.writeToConsole("\n\nGood bye.");
    }

    private void proccessRequest(CommandDescriptor command) {
        client.sendCommand(command);
    }

    private void printHelp() {
        consoleManager.writeToConsole("\nHelp.\n\n");
        for(CommandType type : CommandType.values()) {
            consoleManager.writeToConsole("%s\t%s\t\t%s\n", type.name(), type.getDescription(), type.getExample());
        }
    }
}
