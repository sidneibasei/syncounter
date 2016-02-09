package com.sync.counter.server.protocol.worker;

import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.ResponseMessage.ResponseType;
import com.sync.counter.common.protocol.ResponseMessageBuilder;
import com.sync.counter.common.protocol.parser.ResponseMessageParser;
import com.sync.counter.common.protocol.socket.ByteBufferDelegate;
import com.sync.counter.server.exception.ServerException;
import com.sync.counter.server.protocol.ChannelPayload;
import com.sync.counter.server.protocol.SocketChannelAccepter;
import com.sync.counter.server.service.CounterService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope("prototype")
public class CounterWorkerNode implements Runnable {

	private static final Logger logger = LogManager.getLogger(SocketChannelAccepter.class);

	@Autowired
	private CounterService counterService;
	
	private ChannelPayload channelMessage;

	public void setChannelMessage(ChannelPayload channelMessage) {
		this.channelMessage = channelMessage;
	}

	private final ByteBufferDelegate buffer = ByteBufferDelegate.allocate(32);

	@Override
	public void run() throws ServerException {
		try {
			processRequestAndResponse(channelMessage);
		} catch(ServerException ex) {
			logger.error(String.format("Error processing message for client."), ex);
			throw ex;
		}
	}

	/**
	 * Calls the counterService to get/inc/dec the counter.
	 * @param message
	 * @throws ServerException
	 * @See CounterBean
	 */
	protected void processRequestAndResponse(ChannelPayload message) throws ServerException {
		final ResponseMessageBuilder builder = new ResponseMessageBuilder();
		builder.withType(ResponseType.ok);

		switch (message.getMessage().getType()) {
		case get:
			builder.withValue(counterService.currentValue());
			break;
		case inc:
			builder.withValue(counterService.incrementAndReturn(message.getMessage().getValue()));
			break;
		case dec:
			builder.withValue(counterService.decrementAndReturn(message.getMessage().getValue()));
			break;
		}

		try {
			final ResponseMessage response = builder.build();
			buffer.clear();
			buffer.put(new ResponseMessageParser().toByteArray(response));
			buffer.flip();
			message.getChannel().write(buffer);
			logger.info("Response sent to client. Value = " + response.getValue());
		} catch (IOException e) {
			throw new ServerException("Error sending response to client");
		}
	}
}
