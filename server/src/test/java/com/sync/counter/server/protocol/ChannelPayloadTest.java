package com.sync.counter.server.protocol;

import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.socket.SocketChannelDelegate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class ChannelPayloadTest {

	@Mock
	private SocketChannelDelegate channel;
	
	@Mock
	private RequestMessage message;
	
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
