package com.sync.counter.client;

import com.sync.counter.client.console.CommandLineInterpreter;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@EnableAutoConfiguration
@Component
@ComponentScan("com.sync.counter")
public class Main implements CommandLineRunner {

    @Autowired
    private CommandOptionBean commandOptionBean;

    @Autowired
    private CommandLineInterpreter commandLine;
    
    @Override
    public void run(String[] arguments) {

        try {
            commandOptionBean.initialize(arguments);
        }catch(ParseException ex) {
            ex.printStackTrace();
            System.exit(0);
        }

        if(commandOptionBean.isHelp()) {
            commandOptionBean.showOptions();
            System.exit(0);
        } else {
            commandLine.run();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
