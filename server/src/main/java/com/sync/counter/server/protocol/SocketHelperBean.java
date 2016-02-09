package com.sync.counter.server.protocol;

import com.sync.counter.common.protocol.CounterProtocolContants;
import com.sync.counter.common.protocol.socket.SocketChannelDelegate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Component
public class SocketHelperBean {

    public Selector openSelector() throws IOException {
        return Selector.open();
    }

    public ServerSocketChannel initializeServerChannel(Selector selector) throws IOException {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(CounterProtocolContants.PORT));
        serverSocketChannel.configureBlocking(false);

        final int ops = serverSocketChannel.validOps();
        serverSocketChannel.register(selector, ops, null);
        return serverSocketChannel;
    }

    public SocketChannelDelegate accept(ServerSocketChannel severChannel) throws IOException {
        return new SocketChannelDelegate(severChannel.accept());
    }

    public SocketChannelDelegate selectionKeyChannel(SelectionKey selectionKey) throws IOException {
        return new SocketChannelDelegate((SocketChannel)selectionKey.channel());
    }

    public boolean keepRunning() {
        return true;
    }
}
