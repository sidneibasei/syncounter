package com.sync.counter.client;

import com.sync.counter.client.console.CommandLineInterpreter;
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
    private CommandLineInterpreter commandLine;
    
    @Override
    public void run(String[] arguments) {
    	String remoteIp = "127.0.0.1";
    	if(arguments.length >= 1) {
    		remoteIp = arguments[0];
    	}
    	
    	Boolean random = Boolean.FALSE;
    	
    	if(arguments.length >= 2) {
    		random = "rand".equals(arguments[1]);
    	}
        commandLine.run(remoteIp, random);
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
