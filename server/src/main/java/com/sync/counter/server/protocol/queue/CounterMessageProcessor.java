package com.sync.counter.server.protocol.queue;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.sync.counter.server.protocol.ChannelPayload;
import com.sync.counter.server.protocol.worker.CounterWorkerNode;

@Component
public class CounterMessageProcessor extends Thread {
	
	private static final Logger logger = LogManager.getLogger(CounterMessageProcessor.class);
	
	@Autowired
	private QueueServiceBean queue;
	
	@Autowired 
	private ApplicationContext context;
	
	@Autowired 
	protected ThreadPoolTaskExecutor executor;
		
	@Override
	public void run() {
		while(true) {
			try {
				synchronized (queue) {
					queue.wait(1000); // wait for notify or 1000ms
				}
				Iterator<ChannelPayload> iterator = queue.iterator();
				while(iterator.hasNext()) {
					final ChannelPayload channelMessage = iterator.next();
					final CounterWorkerNode worker = context.getBean(CounterWorkerNode.class); /* gets a new spring bean */
					logger.debug("Got worker = " + worker);
					worker.setChannelMessage(channelMessage);
		            executor.execute(worker);
					iterator.remove();
				}
			} catch(InterruptedException e) {
				logger.error(String.format("Error on thread %s", e.getMessage()), e);
			}
		}
	}
}
