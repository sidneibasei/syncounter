package com.sync.counter.client;

import com.sync.counter.client.console.CommandLineInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by sidnei on 03/02/16.
 */
@EnableAutoConfiguration
@Component
@ComponentScan("com.sync.counter")
public class Main implements CommandLineRunner {

    @Autowired
    private CommandLineInterpreter commandLine;

    @Override
    public void run(String[] args) {
        commandLine.run();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
