package com.sync.counter.server.protocol;

import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.socket.SocketChannelDelegate;

public class ChannelPayload {

	private final SocketChannelDelegate channel;
	private final RequestMessage message;
	
	public ChannelPayload(SocketChannelDelegate channel, RequestMessage message) {
		this.channel = channel;
		this.message = message;
	}
	
	public SocketChannelDelegate getChannel() {
		return channel;
	}
	
	public RequestMessage getMessage() {
		return message;
	}
	
}
