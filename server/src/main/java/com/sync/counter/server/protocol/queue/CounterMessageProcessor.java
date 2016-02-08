package com.sync.counter.server.protocol.queue;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.sync.counter.server.protocol.ChannelPayload;
import com.sync.counter.server.protocol.worker.WorkerNode;

@Component
public class CounterMessageProcessor extends Thread {
	
	private static final Logger logger = LogManager.getLogger(CounterMessageProcessor.class);
	
	@Autowired
	private QueueServiceBean queueServiceBean;
	
	@Autowired 
	private ApplicationContext context;
	
	@Autowired 
	protected ThreadPoolTaskExecutor executor;
		
	@Override
	public void run() {
		while(true) {
			try {
				synchronized (queueServiceBean) {
					queueServiceBean.wait(1000); // wait for notify or 1000ms
				}
				Iterator<ChannelPayload> iter = queueServiceBean.iterator();				
				while(iter.hasNext()) {
					final ChannelPayload channelMessage = iter.next();		
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
}
