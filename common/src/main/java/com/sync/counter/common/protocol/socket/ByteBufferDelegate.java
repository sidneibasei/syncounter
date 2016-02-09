package com.sync.counter.common.protocol.socket;

import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class ByteBufferDelegate {

    private final ByteBuffer buffer;

    private ByteBufferDelegate(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public static ByteBufferDelegate allocate(int capacity) {
        return new ByteBufferDelegate(ByteBuffer.allocate(capacity));
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public Buffer clear() {
        return buffer.clear();
    }

    public Buffer flip() {
        return buffer.flip();
    }

    public ByteBuffer put(byte[] src) {
        return buffer.put(src);
    }

    public void get(byte[] array) throws IOException {
        buffer.get(array);
    }

    public Boolean hasRemaining() throws  IOException {
        return buffer.hasRemaining();
    }
}
