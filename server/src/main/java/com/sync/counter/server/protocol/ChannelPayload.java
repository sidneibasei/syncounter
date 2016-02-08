package com.sync.counter.server.protocol;

import java.nio.channels.SocketChannel;

import com.sync.counter.common.protocol.CounterMessageRequest;

public class ChannelPayload {

	private final SocketChannel channel;
	private final CounterMessageRequest message;
	
	public ChannelPayload(SocketChannel channel, CounterMessageRequest message) {
		this.channel = channel;
		this.message = message;
	}
	
	public SocketChannel getChannel() {
		return channel;
	}
	
	public CounterMessageRequest getMessage() {
		return message;
	}
	
}
