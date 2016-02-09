package com.sync.counter.server.protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.ResponseMessage.ResponseType;
import com.sync.counter.common.protocol.CounterProtocolContants;
import com.sync.counter.common.protocol.ResponseMessageBuilder;
import com.sync.counter.common.protocol.exceptions.WrongMessageException;
import com.sync.counter.common.protocol.parser.RequestMessageParser;
import com.sync.counter.common.protocol.parser.ResponseMessageParser;
import com.sync.counter.server.protocol.queue.CounterMessageProcessor;
import com.sync.counter.server.protocol.queue.QueueServiceBean;

@Component
public class SocketChannelAccepter extends Thread {

	private static final Logger logger = LogManager.getLogger(SocketChannelAccepter.class);

	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private ByteBuffer buffer = ByteBuffer.allocate(1024);

	@Autowired
	private QueueServiceBean queueServiceBean;

	@Override
	public void run() {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(CounterProtocolContants.PORT));
			serverSocketChannel.configureBlocking(false);

			final int ops = serverSocketChannel.validOps();
			serverSocketChannel.register(selector, ops, null);

			while (true) {
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();
				while (iter.hasNext()) {
					SelectionKey selectionKey = iter.next();
					// accept connections 
					try {
						if (selectionKey.isAcceptable()) {
							final SocketChannel channelAccepted = serverSocketChannel.accept();
							processNewClientConnection(channelAccepted);
						} else if(selectionKey.isReadable()) {
							proccessNewClientMessage((SocketChannel)selectionKey.channel());
						}	
					} catch(IOException ex) {
						selectionKey.cancel();
						selectionKey.channel().close();
						logger.error("Error I/O");
					} finally {
						iter.remove();	
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Processes all new messages from clients. It moves all valid messages onto a queue
	 * which will be processed by a Thread Pool on CounterMessageProcessor.
	 * @param channel
	 * @throws IOException
	 * @see CounterMessageProcessor
	 */
	protected void proccessNewClientMessage(final SocketChannel channel) throws IOException {
		Integer size = queueServiceBean.size();
		buffer.clear();
		int read = channel.read(buffer);
		if(read > 0) {
            buffer.flip();
            if (size > CounterProtocolContants.MAX_QUEUE_SIZE) {
				reponseImmediately(channel, ResponseType.serverBusy, size);
            } else {
                try {
                    RequestMessage message = new RequestMessageParser().parse(buffer.array());
                    queueServiceBean.add(new ChannelPayload(channel, message));
                    synchronized (queueServiceBean) {
                    	queueServiceBean.notify();
                    }
                } catch (WrongMessageException e) {
					reponseImmediately(channel, ResponseType.wrongMessage, 0);
					forceCloseChannel(channel);
				}
            }
        } else {
			forceCloseChannel(channel);
		}
	}

	private void forceCloseChannel(SocketChannel channel)  {
		try {
			logger.info(String.format("Closing connection from %s", channel.getRemoteAddress().toString()));
			channel.close();
		}catch(Exception ex) { /* just ignore it */}

	}

	/**
	 * Accepts all new client connections and register them for READ event.
	 * @throws IOException
	 */
	protected void processNewClientConnection(final SocketChannel socketChannel) throws IOException {
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		logger.info(String.format("New client connected from %s", socketChannel.getRemoteAddress().toString()));
	}

	/**
	 * Sends a message to the client immediately.
	 * @param channel
	 * @param type
	 * @param value
	 */
	protected void reponseImmediately(SocketChannel channel, ResponseType type, Integer value) {
		final ResponseMessage response = new ResponseMessageBuilder().withType(type).withValue(value).build();
		try {
			final ByteBuffer buffer = ByteBuffer.allocate(32);
			buffer.put(new ResponseMessageParser().toByteArray(response));
			buffer.flip();
			channel.write(buffer);
		} catch(IOException e) {
			forceCloseChannel(channel);
		}
	}
}