package com.sync.counter.server.protocol.worker;

import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.RequestMessageBuilder;
import com.sync.counter.common.util.CommonUtil;
import com.sync.counter.server.exception.ServerException;
import com.sync.counter.server.protocol.ChannelPayload;
import com.sync.counter.server.service.CounterService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by sidnei on 08/02/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class WorkerNodeTest {

    @Mock
    private CounterService serviceMock;

    @Mock
    private ChannelPayload message;

    @Mock
    private SocketChannel channelMock;

    @InjectMocks
    private CounterWorkerNode unit;

    @Before
    public void setupChannel() {
    }

    @Before
    public void setChannelMessage() {
        when(message.getChannel()).thenReturn(channelMock);
        unit.setChannelMessage(message);
    }

    @Test
    public void testIncFive() throws Exception {
        final RequestMessage requestMessage = new RequestMessageBuilder()
                .withType(RequestMessage.RequestType.inc)
                .withValue(5)
                .build();

        final ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        final ArgumentCaptor<Integer> deltaCaptor = ArgumentCaptor.forClass(Integer.class);
        final ArgumentCaptor<RequestMessage.RequestType> typeCaptor = ArgumentCaptor.forClass(RequestMessage.RequestType.class);


        // mocking
        when(message.getMessage()).thenReturn(requestMessage);
        when(channelMock.write(bufferCaptor.capture())).thenReturn(8);
        when(serviceMock.incrementAndReturn(deltaCaptor.capture())).thenReturn(10);
        when(serviceMock.decrementAndReturn(any(Integer.class))).thenThrow(IllegalAccessException.class);
        when(serviceMock.currentValue()).thenThrow(IllegalAccessException.class);



        //running
        unit.run();

        assertEquals(5, deltaCaptor.getValue().intValue());


        assertArrayEquals(Arrays.copyOf(CommonUtil.createByteSequence(0x00, 10), 32), bufferCaptor.getValue().array());
    }


    @Test
    public void testDecEight() throws Exception {
        final RequestMessage requestMessage = new RequestMessageBuilder()
                .withType(RequestMessage.RequestType.dec)
                .withValue(8)
                .build();

        final ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);
        final ArgumentCaptor<Integer> deltaCaptor = ArgumentCaptor.forClass(Integer.class);


        // mocking
        when(message.getMessage()).thenReturn(requestMessage);
        when(channelMock.write(bufferCaptor.capture())).thenReturn(8);
        when(serviceMock.decrementAndReturn(deltaCaptor.capture())).thenReturn(6546);
        when(serviceMock.incrementAndReturn(any(Integer.class))).thenThrow(IllegalAccessException.class);
        when(serviceMock.currentValue()).thenThrow(IllegalAccessException.class);


        //running
        unit.run();

        assertEquals(8, deltaCaptor.getValue().intValue());
        assertArrayEquals(Arrays.copyOf(CommonUtil.createByteSequence(0x0, 6546), 32), bufferCaptor.getValue().array());

    }

    @Test
    public void testCurrentValue() throws Exception {
        final RequestMessage requestMessage = new RequestMessageBuilder()
                .withType(RequestMessage.RequestType.get)
                .withValue(0)
                .build();

        final ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);


        // mocking
        when(message.getMessage()).thenReturn(requestMessage);
        when(channelMock.write(bufferCaptor.capture())).thenReturn(8);
        when(serviceMock.currentValue()).thenReturn(98799);
        when(serviceMock.incrementAndReturn(any(Integer.class))).thenThrow(IllegalAccessException.class);
        when(serviceMock.decrementAndReturn(any(Integer.class))).thenThrow(IllegalAccessException.class);


        //running
        unit.run();
        assertArrayEquals(Arrays.copyOf(CommonUtil.createByteSequence(0x0, 98799), 32), bufferCaptor.getValue().array());

    }

    @Test(expected = ServerException.class)
    public void testIOExceptionWhileWritingToChannel() throws Exception {
        final RequestMessage requestMessage = new RequestMessageBuilder()
                .withType(RequestMessage.RequestType.get)
                .withValue(0)
                .build();

        final ArgumentCaptor<ByteBuffer> bufferCaptor = ArgumentCaptor.forClass(ByteBuffer.class);

        // mocking
        when(message.getMessage()).thenReturn(requestMessage);
        when(channelMock.write(bufferCaptor.capture())).thenThrow(IOException.class);

        //running
        unit.run();
    }
}
