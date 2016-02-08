package com.sync.counter.client.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.sync.counter.client.console.command.CommandDescriptor;
import com.sync.counter.common.protocol.CounterMessageRequest;
import com.sync.counter.common.protocol.CounterMessageResponse;
import com.sync.counter.common.protocol.CounterProtocolContants;
import com.sync.counter.common.protocol.RequestMessageBuilder;
import com.sync.counter.common.protocol.parser.CounterRequestParser;
import com.sync.counter.common.protocol.parser.CounterResponseParser;

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

    public CounterMessageResponse sendCommand(CommandDescriptor command) throws IOException {
    	if(command.getType().getRequestType() == null) {
    		logger.error(String.format("Invalid command type %s", command.getType()));
    		return null;
    	}
    	
    	final CounterMessageRequest message = new RequestMessageBuilder()
    			.withType(command.getType().getRequestType())
    			.withValue(command.getArgument())
    			.build();

    	if(channel == null) {
    		logger.error("Channel is null");
    		throw new IOException("channel is null");
    	}
    	
    	write(message);
    	
    	inputBuffer.clear();
    	int read = channel.read(inputBuffer);
    	
    	
    	logger.info(String.format("Read %d bytes from the server", read));
    	
    	return new CounterResponseParser().parse(inputBuffer.array());
    }
    
    /**
     * writes the message to the channel 
     * @param message
     * @throws IOException
     */
    protected void write(CounterMessageRequest message) throws IOException {
    	outputBuffer.clear();
    	byte[] messageBytes = new CounterRequestParser().toByteArray(message);
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
