package com.sync.counter.client.console;

import com.sync.counter.client.console.command.CommandDescriptor;
import com.sync.counter.client.console.command.CommandParser;
import com.sync.counter.client.console.command.CommandType;
import com.sync.counter.client.console.exception.InvalidInputException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sidnei on 04/02/16.
 */


public class CommandParserTest {

    @Test
    public void testTestCommandHelp() throws InvalidInputException {
        CommandDescriptor commandDescriptor = new CommandParser().parse("help");
        assertEquals(CommandType.help, commandDescriptor.getType());
        assertNull(commandDescriptor.getArgument());
    }

    @Test
    public void testTestCommandExit() throws InvalidInputException {
        CommandDescriptor commandDescriptor = new CommandParser().parse("exit");
        assertEquals(CommandType.exit, commandDescriptor.getType());
        assertNull(commandDescriptor.getArgument());
    }

    @Test(expected = InvalidInputException.class)
    public void testTestCommandExitWithArg() throws InvalidInputException {
        new CommandParser().parse("exit 2");
        fail("InvalidInputException expected");
    }

    @Test(expected = InvalidInputException.class)
    public void testTestCommandHelpWithArg() throws InvalidInputException {
        new CommandParser().parse("help 3");
        fail("InvalidInputException expected");
    }

    @Test
    public void testTestCommandInc() throws InvalidInputException {
        CommandDescriptor commandDescriptor = new CommandParser().parse("inc");
        assertEquals(CommandType.inc, commandDescriptor.getType());
        assertNull(commandDescriptor.getArgument());
    }

    @Test
    public void testTestCommandDec() throws InvalidInputException {
        CommandDescriptor commandDescriptor = new CommandParser().parse("dec");
        assertEquals(CommandType.dec, commandDescriptor.getType());
        assertNull(commandDescriptor.getArgument());
    }

    @Test
    public void testTestCommandIncWithArgument() throws InvalidInputException {
        CommandDescriptor commandDescriptor = new CommandParser().parse("inc 2");
        assertEquals(CommandType.inc, commandDescriptor.getType());
        assertEquals(2, commandDescriptor.getArgument().intValue());
    }

    @Test
    public void testTestCommandDecWithArgument() throws InvalidInputException {
        CommandDescriptor commandDescriptor = new CommandParser().parse("dec 3");
        assertEquals(CommandType.dec, commandDescriptor.getType());
        assertEquals(3, commandDescriptor.getArgument().intValue());
    }

    @Test(expected = InvalidInputException.class)
    public void testTestCommandWithLetterArgument() throws InvalidInputException {
        new CommandParser().parse("inc a");
        fail("InvalidInputException expected");

    }

    @Test(expected = InvalidInputException.class)
    public void testTestCommandDecWithLetterArgument() throws InvalidInputException {
        new CommandParser().parse("dec b");
        fail("InvalidInputException expected");
    }
}
