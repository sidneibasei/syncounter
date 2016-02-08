package com.sync.counter.server;

import com.sync.counter.server.protocol.SyncCounterServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by sidnei on 08/02/16.
 */
@EnableAutoConfiguration
@Component
@ComponentScan("com.sync.counter")
public class SpringBoot implements CommandLineRunner {

    @Autowired
    private SyncCounterServer counterServer;

    @Override
    public void run(String... args) throws Exception {
        counterServer.startServer();
    }
}
