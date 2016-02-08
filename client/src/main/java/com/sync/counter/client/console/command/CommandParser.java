package com.sync.counter.client.console.command;

import com.sync.counter.client.console.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sidnei on 03/02/16.
 */
@Component
public class CommandParser {

    private static final String COMMAND_REGEXP[] = new String [] {
            "^(?<command>inc)( (?<arg>[0-9]+))?$",
            "^(?<command>dec)( (?<arg>[0-9]+))?$",
            "^(?<command>exit(?<arg>))$",
            "^(?<command>help(?<arg>))$",
            "^(?<command>get(?<arg>))$"
    };
    private static final String ARG = "arg";
    private static final String COMMAND = "command";

    public CommandDescriptor parse(String command) throws InvalidInputException {
        for(String regexp : COMMAND_REGEXP) {
            final Pattern pattern = Pattern.compile(regexp);
            final Matcher matcher = pattern.matcher(command.trim().toLowerCase());
            if(matcher.matches()) {
                CommandType type = CommandType.valueOf(matcher.group(COMMAND));
                Integer n = null;
                if (matcher.group(ARG) != null && !matcher.group(ARG).trim().equals("")) {
                    n = Integer.parseInt(matcher.group(ARG));
                }
                return new CommandDescriptor(type, n);
            }
        }
        throw new InvalidInputException(command);
    }
}
