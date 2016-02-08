package com.sync.counter.server.protocol.worker;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.sync.counter.server.service.CounterService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sync.counter.common.protocol.CounterMessageResponse;
import com.sync.counter.common.protocol.CounterMessageResponse.ResponseType;
import com.sync.counter.common.protocol.ResponseMessageBuilder;
import com.sync.counter.common.protocol.parser.CounterResponseParser;
import com.sync.counter.server.exception.ServerException;
import com.sync.counter.server.protocol.ChannelMessage;
import com.sync.counter.server.protocol.SocketChannelAccepter;

@Component
@Scope("prototype")
public class WorkerNode implements Runnable {

	private static final Logger logger = LogManager.getLogger(SocketChannelAccepter.class);

	@Autowired
	private CounterService counterService;
	
	private ChannelMessage channelMessage;

	public void setChannelMessage(ChannelMessage channelMessage) {
		this.channelMessage = channelMessage;
	}

	private final ByteBuffer bufferWrite = ByteBuffer.allocate(32);

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
	protected void processRequestAndResponse(ChannelMessage message) throws ServerException {
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
			final CounterMessageResponse response = builder.build();
			bufferWrite.clear();
			bufferWrite.put(new CounterResponseParser().toByteArray(response));
			bufferWrite.flip();
			message.getChannel().write(bufferWrite);
			logger.info("Response sent to client. Value = " + response.getValue());
		} catch (IOException e) {
			throw new ServerException("Error sending response to client");
		}
	}
}
