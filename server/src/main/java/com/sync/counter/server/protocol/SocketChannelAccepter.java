package com.sync.counter.server.protocol;

import com.sync.counter.common.protocol.CounterProtocolContants;
import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.ResponseMessage.ResponseType;
import com.sync.counter.common.protocol.ResponseMessageBuilder;
import com.sync.counter.common.protocol.exceptions.WrongMessageException;
import com.sync.counter.common.protocol.parser.RequestMessageParser;
import com.sync.counter.common.protocol.parser.ResponseMessageParser;
import com.sync.counter.common.protocol.socket.ByteBufferDelegate;
import com.sync.counter.common.protocol.socket.SocketChannelDelegate;
import com.sync.counter.server.protocol.queue.CounterMessageProcessor;
import com.sync.counter.server.protocol.queue.QueueServiceBean;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

@Component
public class SocketChannelAccepter extends Thread {

	private static final Logger logger = LogManager.getLogger(SocketChannelAccepter.class);

	private ByteBufferDelegate buffer = ByteBufferDelegate.allocate(1024);

	@Autowired
	private QueueServiceBean queueServiceBean;

	@Autowired
	private SocketHelperBean socketHelper;


	@Override
	public void run() {
		try {
			final Selector selector = socketHelper.openSelector();
			final ServerSocketChannel serverSocketChannel = socketHelper.initializeServerChannel(selector);

			while (socketHelper.keepRunning()) {
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();
				while (iter.hasNext()) {
					final SelectionKey selectionKey = iter.next();
					// accept connections
					try {
						if (selectionKey.isAcceptable()) {
							final SocketChannelDelegate channel = socketHelper.accept(serverSocketChannel);
							processNewClientConnection(selector, channel);
						} else if(selectionKey.isReadable()) {
							final SocketChannelDelegate channel = socketHelper.selectionKeyChannel(selectionKey);
							proccessNewClientMessage(channel);
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
	protected void proccessNewClientMessage(final SocketChannelDelegate channel) throws IOException {
		Integer size = queueServiceBean.size();
		buffer.clear();
		int read = channel.read(buffer);
		if(read > 0) {
            buffer.flip();
            if (size > CounterProtocolContants.MAX_QUEUE_SIZE) {
				reponseImmediately(channel, ResponseType.serverBusy, size);
            } else {
                try {
					byte array[] = new byte[read];
					buffer.get(array);
                    RequestMessage message = new RequestMessageParser().parse(array);
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

	private void forceCloseChannel(SocketChannelDelegate channel)  {
		try {
			logger.info(String.format("Closing connection from %s", channel.getRemoteAddress()));
			channel.close();
		}catch(Exception ex) { ex.printStackTrace();/* just ignore it */}

	}

	/**
	 * Accepts all new client connections and register them for READ event.
	 * @throws IOException
	 */
	protected void processNewClientConnection(final Selector selector, final SocketChannelDelegate socketChannel) throws IOException {
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		logger.info(String.format("New client connected from %s", socketChannel.getRemoteAddress()));
	}

	/**
	 * Sends a message to the client immediately.
	 * @param channel
	 * @param type
	 * @param value
	 */
	protected void reponseImmediately(SocketChannelDelegate channel, ResponseType type, Integer value) {
		final ResponseMessage response = new ResponseMessageBuilder().withType(type).withValue(value).build();
		try {
			final ByteBufferDelegate buffer = ByteBufferDelegate.allocate(32);
			buffer.put(new ResponseMessageParser().toByteArray(response));
			buffer.flip();
			channel.write(buffer);
		} catch(IOException e) {
			forceCloseChannel(channel);
		}
	}




}