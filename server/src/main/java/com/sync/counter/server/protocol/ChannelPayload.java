package com.sync.counter.server.protocol;

import java.nio.channels.SocketChannel;

import com.sync.counter.common.protocol.RequestMessage;

public class ChannelPayload {

	private final SocketChannel channel;
	private final RequestMessage message;
	
	public ChannelPayload(SocketChannel channel, RequestMessage message) {
		this.channel = channel;
		this.message = message;
	}
	
	public SocketChannel getChannel() {
		return channel;
	}
	
	public RequestMessage getMessage() {
		return message;
	}
	
}
