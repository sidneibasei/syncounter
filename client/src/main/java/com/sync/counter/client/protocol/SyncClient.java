package com.sync.counter.client.protocol;

import com.sync.counter.client.console.ConsoleManager;
import com.sync.counter.client.console.command.CommandDescriptor;
import com.sync.counter.common.protocol.CounterMessage;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by sidnei on 04/02/16.
 */
@Component
public class SyncClient {

    @Autowired
    private ConsoleManager consoleManager;


    public void openConnection() {
    }

    public Integer sendCommand(CommandDescriptor command) {


        CounterMessage message = new CounterMessage(command.getType().getMessageType(), command.getArgument());

        try {
            consoleManager.writeToConsole("Sending message %s", Hex.encodeHexString(message.getMessage()));
        }catch (IOException e) {

        }


        return 0;
    }

    public void closeConnection() {
    }

}
