package com.sync.counter.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.sync.counter.server.protocol.CounterServer;

@EnableAutoConfiguration
@Component
@ComponentScan("com.sync.counter")
public class SpringBoot implements CommandLineRunner {

    @Autowired
    private CounterServer counterServer;

    @Override
    public void run(String... args) throws Exception {
        counterServer.startServer();
    }
}
