package com.sync.counter.client.parser;

import java.util.StringTokenizer;

/**
 * Created by sidnei on 03/02/16.
 */
public class CommandParser {

    private static final String INCREMENT_REGEXP = "^\\ .*(increment)\\ .*([0-9]|(\\ .*)$)$";
    private static final String DECREMENT_REGEXP = "^\\ .*(increment)\\ .*([0-9]|(\\ .*))$";
    private static final String GETVALUE_REGEXP = "^\\ .*(value)\\ .*)$";

    private final StringTokenizer tokenizer;

    public CommandParser(String command) {
        tokenizer = new StringTokenizer(command.toLowerCase());
    }


    public enum CommandToken {
        increment,
        decrement,
        value
    }

    public class Command {
        private final CommandToken token;
        private final Integer value;

        public Command(CommandToken token) {
            this(token, 1);
        }

        public Command(CommandToken token, Integer value) {
            this.token = token;
            this.value = value;
        }
    }

    public void parse() {


//
//
//
//        String command = tokenizer.nextToken();
//
//        Token token =
//
//        if(Tokens.open.name().equals(command)) {
//            processOpenCommand();
//        }
//
//
//        while (tokenizer.hasMoreElements()) {
//            System.out.println(tokenizer.nextElement());
//        }

    }
}
