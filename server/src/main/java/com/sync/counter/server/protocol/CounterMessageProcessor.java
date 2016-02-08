package com.sync.counter.server.protocol;

import java.util.Iterator;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.sync.counter.server.protocol.worker.WorkerNode;

@Component
public class CounterMessageProcessor extends Thread {
	
	private static final Logger logger = LogManager.getLogger(CounterMessageProcessor.class);
	
	private Queue<ChannelMessage> queue;
	
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
				Iterator<ChannelMessage> iter = queue.iterator();				
				while(iter.hasNext()) {
					final ChannelMessage channelMessage = iter.next();		
					final WorkerNode worker = context.getBean(WorkerNode.class);
					logger.info("Got worker = " + worker);
					worker.setChannelMessage(channelMessage);
		            executor.execute(worker);
					iter.remove();
				}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setQueue(Queue<ChannelMessage> queue) {
		this.queue = queue;
	}
}
