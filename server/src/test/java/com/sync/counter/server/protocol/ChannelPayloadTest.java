package com.sync.counter.server.protocol;

import java.nio.channels.SocketChannel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sync.counter.common.protocol.CounterMessageRequest;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChannelPayloadTest {

	@Mock
	private SocketChannel channel;
	
	@Mock
	private CounterMessageRequest message;
	
	@Test
	public void testConstructor () {
		final ChannelPayload channelMessage = new ChannelPayload(channel, message);
		assertSame(channel,channelMessage.getChannel());
		assertSame(message,channelMessage.getMessage());
	}
	
	@Test
	public void testConstructorWithNulLValues () {
		final ChannelPayload channelMessage = new ChannelPayload(null, null);
		assertNull(channelMessage.getChannel());
		assertNull(channelMessage.getMessage());
	}
	
}
