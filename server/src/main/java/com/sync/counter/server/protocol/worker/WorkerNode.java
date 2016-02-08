package com.sync.counter.server.protocol.worker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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
import com.sync.counter.server.service.CounterBean;

@Component
@Scope("prototype")
public class WorkerNode implements Runnable {

	private static final Logger logger = LogManager.getLogger(SocketChannelAccepter.class);

	@Autowired
	private CounterBean counterBean;
	
	private ChannelMessage channelMessage;

	public void setChannelMessage(ChannelMessage channelMessage) {
		this.channelMessage = channelMessage;
	}

	private ByteBuffer bufferWrite = ByteBuffer.allocate(32);

	@Override
	public void run() {
		if (channelMessage.getChannel().isConnected() && channelMessage.getChannel().isOpen()) {
			try {
				processRequestAndResponse(channelMessage);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			closeChannel(channelMessage.getChannel());
		}

	}

	protected void processRequestAndResponse(ChannelMessage message) throws ServerException {
		
		
		
		final ResponseMessageBuilder builder = new ResponseMessageBuilder();
		builder.withType(ResponseType.ok);

		switch (message.getMessage().getType()) {
		case get:
			builder.withValue(counterBean.currentValue());
			break;
		case inc:
			builder.withValue(counterBean.incrementAndReturn(message.getMessage().getValue()));
			break;
		case dec:
			builder.withValue(counterBean.decrementAndReturn(message.getMessage().getValue()));
			break;
		}

		try {
			if(!message.getChannel().isOpen()) {
				return;
			}
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

	protected void closeChannel(SocketChannel channel) {
		try {
			channel.close();
		} catch (IOException e) {
		}
	}

}
