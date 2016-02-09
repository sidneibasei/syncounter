package com.sync.counter.client.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.sync.counter.common.protocol.exceptions.WrongMessageException;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sync.counter.client.console.command.CommandDescriptor;
import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.CounterProtocolContants;
import com.sync.counter.common.protocol.RequestMessageBuilder;
import com.sync.counter.common.protocol.parser.RequestMessageParser;
import com.sync.counter.common.protocol.parser.ResponseMessageParser;

/**
 * Created by sidnei on 04/02/16.
 */
@Component
public class SyncClient {
	
    private static final Logger logger = LogManager.getLogger(SyncClient.class);

    
    private SocketChannel channel;
    private ByteBuffer inputBuffer = ByteBuffer.allocate(32);
    private ByteBuffer outputBuffer = ByteBuffer.allocate(32);

    public void openConnection(String ip) throws IOException {
    	channel = SocketChannel.open(new InetSocketAddress(ip, CounterProtocolContants.PORT));
    }

    public ResponseMessage sendCommand(CommandDescriptor command) throws IOException, WrongMessageException {
    	if(command.getType().getRequestType() == null) {
    		logger.error(String.format("Invalid command type %s", command.getType()));
    		return null;
    	}

		RequestMessageBuilder builder = new RequestMessageBuilder().withType(command.getType().getRequestType());
		if(command.getArgument() != null) {
			builder = builder.withValue(command.getArgument());
		}
    	
    	final RequestMessage message = builder.build();

    	if(channel == null) {
    		logger.error("Channel is null");
    		throw new IOException("channel is null");
    	}
    	
    	write(message);
    	
    	inputBuffer.clear();
    	int read = channel.read(inputBuffer);
    	
    	
    	logger.info(String.format("Read %d bytes from the server", read));
    	
    	return new ResponseMessageParser().parse(inputBuffer.array());
    }
    
    /**
     * writes the message to the channel 
     * @param message
     * @throws IOException
     */
    protected void write(RequestMessage message) throws IOException {
    	outputBuffer.clear();
    	byte[] messageBytes = new RequestMessageParser().toByteArray(message);
    	outputBuffer.put(messageBytes);
        logger.info(String.format("Sending message %s", Hex.encodeHexString(messageBytes)));
		outputBuffer.flip();
    	int wrote = 0;
    	while (outputBuffer.hasRemaining()) {
            wrote += channel.write(outputBuffer);
        }
    	logger.info(String.format("Wrote %d bytes to the channel", wrote));
    }
}
