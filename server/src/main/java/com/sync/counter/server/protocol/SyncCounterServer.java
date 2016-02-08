package com.sync.counter.server.protocol;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sync.counter.common.protocol.CounterProtocolContants;

@Component
public class SyncCounterServer {
	
    private static final Logger logger = LogManager.getLogger(SyncCounterServer.class);

	private Queue<ChannelMessage> socketQueue = new ArrayBlockingQueue<ChannelMessage>(1024);
	
	@Autowired
	private SocketChannelAccepter socketChannelAccepter;
	
	@Autowired
	private CounterMessageProcessor counterMessageProcessor;
	
	public void startServer() throws IOException {
		logger.info("Starting server on port %s", CounterProtocolContants.PORT);
		
		socketChannelAccepter.setQueue(socketQueue);
		counterMessageProcessor.setQueue(socketQueue);
		
		socketChannelAccepter.start();
		counterMessageProcessor.start();
		
		logger.info("Server is running and accepting connections on port %s", CounterProtocolContants.PORT);		
	}
}
