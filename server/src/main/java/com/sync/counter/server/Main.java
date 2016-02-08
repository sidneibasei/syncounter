package com.sync.counter.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.sync.counter.server.protocol.SyncCounterServer;


@EnableAutoConfiguration
@Component
@ComponentScan("com.sync.counter")
public class Main implements CommandLineRunner {
	
	@Autowired
	private SyncCounterServer counterServer;
	
	
	@Override
	public void run(String... args) throws Exception {
		counterServer.startServer();
	}

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
