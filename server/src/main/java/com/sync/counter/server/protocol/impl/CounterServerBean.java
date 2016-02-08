package com.sync.counter.server.protocol.impl;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sync.counter.common.protocol.CounterProtocolContants;
import com.sync.counter.server.protocol.CounterServer;
import com.sync.counter.server.protocol.SocketChannelAccepter;
import com.sync.counter.server.protocol.queue.CounterMessageProcessor;

@Component
public class CounterServerBean implements CounterServer {
	
    private static final Logger logger = LogManager.getLogger(CounterServerBean.class);

	@Autowired
	private SocketChannelAccepter socketChannelAccepter;
	
	@Autowired
	private CounterMessageProcessor counterMessageProcessor;
	
	@Override
	public void startServer() throws IOException {
		logger.info(String.format("Starting server on port %s", CounterProtocolContants.PORT));
		
		socketChannelAccepter.start();
		counterMessageProcessor.start();
		
		logger.info(String.format("Server is now running and accepting connections on port %s", CounterProtocolContants.PORT));		
	}
}
