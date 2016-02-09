package com.sync.counter.server.protocol;

import com.sync.counter.server.protocol.impl.CounterServerBean;
import com.sync.counter.server.protocol.queue.CounterMessageProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Queue;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CounterServerBeanTest {

	@Mock
	private SocketChannelAccepter socketChannelAccepter;
	
	@Mock
	private CounterMessageProcessor counterMessageProcessor;
	
	@Mock
	private Queue<ChannelPayload> queueMock;
	
	@InjectMocks
	private CounterServerBean unit;
	
	@Test
	/* ensure that the two main threads have been initialized */
	public void testMethodCall() throws IOException {
		unit.startServer();
		verify(counterMessageProcessor, times(1)).start();
		verify(socketChannelAccepter, times(1)).start();
	}
}
