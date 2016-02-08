package com.sync.counter.server.protocol.queue;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import org.springframework.stereotype.Component;

import com.sync.counter.server.protocol.ChannelPayload;

@Component
public class QueueServiceBean {
	
	private Queue<ChannelPayload> queue = new ArrayBlockingQueue<ChannelPayload>(1024);

	public void add(ChannelPayload payload) {
		queue.add(payload);
	}
	
	public Integer size() {
		return queue.size();
	}
	
	public Iterator<ChannelPayload> iterator() {
		return queue.iterator();
	}
}
