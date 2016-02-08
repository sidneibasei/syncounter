package com.sync.counter.client.console;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sync.counter.client.console.command.CommandDescriptor;
import com.sync.counter.client.console.command.CommandParser;
import com.sync.counter.client.console.command.CommandType;
import com.sync.counter.client.console.exception.InvalidInputException;
import com.sync.counter.client.protocol.SyncClient;
import com.sync.counter.common.protocol.CounterMessageResponse;

/**
 * Created by sidnei on 04/02/16.
 */
@Component
public class CommandLineInterpreter {

    @Autowired
    private ConsoleManager consoleManager;
    private static final Logger logger = LogManager.getLogger(CommandLineInterpreter.class);

    
    @Autowired
    private SyncClient client;

    public void run(String remoteIp, Boolean generateRandom) {
    	
    	try {
    		logger.info(String.format("Opening connection to %s", remoteIp));
    		client.openConnection(remoteIp);
    	}catch(IOException ex) {
    		logger.error(String.format("Error connecting to server %s", remoteIp));
    	}
    	if(generateRandom == false) {
    		executeCommandLine(); 
    	}else {
    		executeRandom();
    	}
    }
    
    protected void executeRandom() {
    	try {
    		CommandDescriptor cd = new CommandDescriptor(CommandType.get, 0);
    		proccessRequest(cd);
			Thread.sleep(1000); //1000ms
		}catch(InterruptedException ex) {}
    	
    	// call n times
    	for(int i = 1000; i>=0; i--) {
    		Boolean inc = true;//((int)(Math.random()*1000) % 2) == 0;
    		Integer value = 1;//Integer.valueOf((int)(Math.random()*10));
    		CommandDescriptor cd = new CommandDescriptor(inc ? CommandType.inc : CommandType.dec, value);
    		proccessRequest(cd);
    		try {
    			Thread.sleep(10); //400ms
    		}catch(InterruptedException ex) {}
    	}
    }

	protected void executeCommandLine() {
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

    protected void exitSystem() {
        consoleManager.writeToConsole("\n\nGood bye.");
    }

    protected void proccessRequest(CommandDescriptor command) {
    	try {
    		CounterMessageResponse response = client.sendCommand(command);
    		if(response.isOk()) {
    			consoleManager.writeToConsole(String.format("OK: Value: %s", response.getValue()));
    		} else {
        		consoleManager.writeToConsole(String.format("ERROR: " , response.getErrorMessage()));
    		}
    	} catch(IOException ex) {
    		consoleManager.writeToConsole(String.format("Error writing / reading from server. % s", ex.getMessage()));
        }
    }

    protected void printHelp() {
        consoleManager.writeToConsole("\nHelp.\n\n");
        for(CommandType type : CommandType.values()) {
            consoleManager.writeToConsole(String.format("%s\t%s\t\t%s\n", type.name(), type.getDescription(), type.getExample()));
        }
    }
}
