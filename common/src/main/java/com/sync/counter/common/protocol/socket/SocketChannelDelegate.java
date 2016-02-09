package com.sync.counter.common.protocol.socket;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SocketChannelDelegate {

    private final SocketChannel channel;

    public SocketChannelDelegate(SocketChannel channel) {
        this.channel = channel;
    }

    public int write(ByteBufferDelegate src) throws IOException {
        return channel.write(src.getBuffer());
    }

    public int read(ByteBufferDelegate dst) throws IOException {
        return channel.read(dst.getBuffer());
    }

    public void close() throws IOException {
        channel.close();
    }

    public boolean isOpen() {
        return channel.isOpen();
    }

    public SocketAddress getRemoteAddress() throws IOException {
        return channel.getRemoteAddress();
    }

    public void configureBlocking(Boolean blocking) throws IOException {
        channel.configureBlocking(blocking);
    }

    public void register(Selector selector, int op) throws IOException {
        channel.register(selector, op);
    }

}
