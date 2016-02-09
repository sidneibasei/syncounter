package com.sync.counter.server.protocol;

import com.sync.counter.common.protocol.CounterProtocolContants;
import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.RequestMessageBuilder;
import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.parser.RequestMessageParser;
import com.sync.counter.common.protocol.socket.ByteBufferDelegate;
import com.sync.counter.common.protocol.socket.SocketChannelDelegate;
import com.sync.counter.common.util.CommonUtil;
import com.sync.counter.server.protocol.queue.QueueServiceBean;
import com.sync.counter.server.spring.AppConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.channels.*;
import java.util.HashSet;
import java.util.Set;

import static com.sync.counter.common.protocol.RequestMessage.RequestType.inc;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static com.sync.counter.common.protocol.RequestMessage.RequestType.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class SocketChannelAccepterTest {

    @Spy
    private QueueServiceBean queueServiceBean;

    @Mock
    private SocketHelperBean socketHelperBean;

    @Mock
    private SocketChannelDelegate channel;

    @Autowired
    @InjectMocks
    private SocketChannelAccepter unit;

    @Before
    public void injectMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProccessNewClientMessageInc() throws IOException {
        ArgumentCaptor<ChannelPayload> channelPayloadArgumentCaptor = ArgumentCaptor.forClass(ChannelPayload.class);

        final SocketChannelDelegate channel = Mockito.mock(SocketChannelDelegate.class);
        final RequestMessage requestMessage = new RequestMessageBuilder().withType(inc).withValue(6542).build();

        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                if(invocationOnMock.getArguments()[0] instanceof ByteBufferDelegate) {
                    ByteBufferDelegate buffer = (ByteBufferDelegate)invocationOnMock.getArguments()[0];
                    byte array[] = new RequestMessageParser().toByteArray(requestMessage);
                    buffer.put(array);
                    return array.length;
                }
                throw new IllegalAccessException();
            }
        }).when(channel).read(any(ByteBufferDelegate.class));

        doCallRealMethod().when(queueServiceBean).add(channelPayloadArgumentCaptor.capture());

        unit.proccessNewClientMessage(channel);

        assertEquals(6542, channelPayloadArgumentCaptor.getValue().getMessage().getValue().intValue());
        assertEquals(inc, channelPayloadArgumentCaptor.getValue().getMessage().getType());
        verify(channel, times(0)).close();

    }


    @Test
    public void testProccessNewClientMessageDec() throws IOException {
        ArgumentCaptor<ChannelPayload> channelPayloadArgumentCaptor = ArgumentCaptor.forClass(ChannelPayload.class);

        final SocketChannelDelegate channel = Mockito.mock(SocketChannelDelegate.class);
        final RequestMessage requestMessage = new RequestMessageBuilder().withType(dec).withValue(65446).build();

        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                if(invocationOnMock.getArguments()[0] instanceof ByteBufferDelegate) {
                    ByteBufferDelegate buffer = (ByteBufferDelegate)invocationOnMock.getArguments()[0];
                    byte array[] = new RequestMessageParser().toByteArray(requestMessage);
                    buffer.put(array);
                    return array.length;
                }
                throw new IllegalAccessException();
            }
        }).when(channel).read(any(ByteBufferDelegate.class));

        doCallRealMethod().when(queueServiceBean).add(channelPayloadArgumentCaptor.capture());

        unit.proccessNewClientMessage(channel);

        assertEquals(65446, channelPayloadArgumentCaptor.getValue().getMessage().getValue().intValue());
        assertEquals(dec, channelPayloadArgumentCaptor.getValue().getMessage().getType());
        verify(channel, times(0)).close();

    }


    @Test
    public void testProccessNewClientMessageGet() throws IOException {
        ArgumentCaptor<ChannelPayload> channelPayloadArgumentCaptor = ArgumentCaptor.forClass(ChannelPayload.class);

        final SocketChannelDelegate channel = Mockito.mock(SocketChannelDelegate.class);
        final RequestMessage requestMessage = new RequestMessageBuilder().withType(get).build();

        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (invocationOnMock.getArguments()[0] instanceof ByteBufferDelegate) {
                    ByteBufferDelegate buffer = (ByteBufferDelegate) invocationOnMock.getArguments()[0];
                    byte array[] = new RequestMessageParser().toByteArray(requestMessage);
                    buffer.put(array);
                    return array.length;
                }
                throw new IllegalAccessException();
            }
        }).when(channel).read(any(ByteBufferDelegate.class));

        doCallRealMethod().when(queueServiceBean).add(channelPayloadArgumentCaptor.capture());

        unit.proccessNewClientMessage(channel);

        assertEquals(0, channelPayloadArgumentCaptor.getValue().getMessage().getValue().intValue());
        assertEquals(get, channelPayloadArgumentCaptor.getValue().getMessage().getType());
        verify(channel, times(0)).close();

    }

    @Test
    public void testServerBusy() throws IOException {
        ArgumentCaptor<ByteBufferDelegate> bufferArgumentCaptor = ArgumentCaptor.forClass(ByteBufferDelegate.class);
        ArgumentCaptor<ChannelPayload> channelPayloadArgumentCaptor = ArgumentCaptor.forClass(ChannelPayload.class);

        final SocketChannelDelegate channel = Mockito.mock(SocketChannelDelegate.class);
        final RequestMessage requestMessage = new RequestMessageBuilder().withType(get).build();

        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (invocationOnMock.getArguments()[0] instanceof ByteBufferDelegate) {
                    ByteBufferDelegate buffer = (ByteBufferDelegate) invocationOnMock.getArguments()[0];
                    byte array[] = new RequestMessageParser().toByteArray(requestMessage);
                    buffer.put(array);
                    return array.length;
                }
                throw new IllegalAccessException();
            }
        }).when(channel).read(any(ByteBufferDelegate.class));

        doCallRealMethod().when(queueServiceBean).add(channelPayloadArgumentCaptor.capture());
        when(channel.write(bufferArgumentCaptor.capture())).thenReturn(8);
        when(queueServiceBean.size()).thenReturn(CounterProtocolContants.MAX_QUEUE_SIZE + 1);

        unit.proccessNewClientMessage(channel);
        byte[] array = new byte[8];
        bufferArgumentCaptor.getValue().get(array);
        assertFalse(bufferArgumentCaptor.getValue().hasRemaining());
        assertArrayEquals(CommonUtil.createByteSequence(ResponseMessage.ResponseType.serverBusy.ordinal(), (CounterProtocolContants.MAX_QUEUE_SIZE + 1)), array);
        verify(channel, times(0)).close();

    }


    @Test
    public void testWrongMessage() throws IOException {
        ArgumentCaptor<ByteBufferDelegate> bufferArgumentCaptor = ArgumentCaptor.forClass(ByteBufferDelegate.class);
        ArgumentCaptor<ChannelPayload> channelPayloadArgumentCaptor = ArgumentCaptor.forClass(ChannelPayload.class);

        final SocketChannelDelegate channel = Mockito.mock(SocketChannelDelegate.class);

        doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (invocationOnMock.getArguments()[0] instanceof ByteBufferDelegate) {
                    ByteBufferDelegate buffer = (ByteBufferDelegate) invocationOnMock.getArguments()[0];
                    byte array[] = CommonUtil.createByteSequence(3, 654);
                    buffer.put(array);
                    return array.length;
                }
                throw new IllegalAccessException();
            }
        }).when(channel).read(any(ByteBufferDelegate.class));

        doCallRealMethod().when(queueServiceBean).add(channelPayloadArgumentCaptor.capture());
        when(channel.write(bufferArgumentCaptor.capture())).thenReturn(8);

        unit.proccessNewClientMessage(channel);
        byte[] array = new byte[8];
        bufferArgumentCaptor.getValue().get(array);
        assertFalse(bufferArgumentCaptor.getValue().hasRemaining());
        assertArrayEquals(CommonUtil.createByteSequence(ResponseMessage.ResponseType.wrongMessage.ordinal(), 0), array);
        verify(channel, times(1)).close();
    }

    @Test
    public void testReadZero() throws IOException {
        ArgumentCaptor<ByteBufferDelegate> bufferArgumentCaptor = ArgumentCaptor.forClass(ByteBufferDelegate.class);
        ArgumentCaptor<ChannelPayload> channelPayloadArgumentCaptor = ArgumentCaptor.forClass(ChannelPayload.class);

        final SocketChannelDelegate channel = Mockito.mock(SocketChannelDelegate.class);
        doReturn(0).when(channel).read(any(ByteBufferDelegate.class));

        doCallRealMethod().when(queueServiceBean).add(channelPayloadArgumentCaptor.capture());
        when(channel.write(bufferArgumentCaptor.capture())).thenReturn(8);

        unit.proccessNewClientMessage(channel);
        verify(channel, times(1)).close();
    }


    @Test
    public void testAcceptNewClient() throws IOException {
        ArgumentCaptor<Boolean> configureCaptor  = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<Selector> selectorCaptor  = ArgumentCaptor.forClass(Selector.class);
        ArgumentCaptor<Integer> opCaptor  = ArgumentCaptor.forClass(Integer.class);


        Selector selectorMock = Mockito.mock(Selector.class);


        doNothing().when(channel).configureBlocking(configureCaptor.capture());
        doNothing().when(channel).register(selectorCaptor.capture(), opCaptor.capture());

        unit.processNewClientConnection(selectorMock, channel);

        assertSame(selectorMock, selectorCaptor.getValue());
        assertEquals(SelectionKey.OP_READ, opCaptor.getValue().intValue());
        assertEquals(Boolean.FALSE, configureCaptor.getValue());
    }


    @Test
    public void testRunNewConnection() throws IOException {
        final ArgumentCaptor<Selector> selectorCaptor = ArgumentCaptor.forClass(Selector.class);
        final ArgumentCaptor<SocketChannelDelegate> channelCaptor = ArgumentCaptor.forClass(SocketChannelDelegate.class);

        final SocketChannelAccepter spiedUnit = spy(unit);

        final Selector selectorMock = Mockito.mock(Selector.class);
        final ServerSocketChannel serverChannelMock = Mockito.mock(ServerSocketChannel.class);

        when(selectorMock.select()).thenReturn(0);

        doNothing().when(channel).configureBlocking(anyBoolean());
        doNothing().when(channel).register(any(Selector.class), anyInt());

        when(socketHelperBean.accept(any(ServerSocketChannel.class))).thenReturn(channel);
        when(socketHelperBean.keepRunning()).thenReturn(true, false);
        when(socketHelperBean.openSelector()).thenReturn(selectorMock);
        when(socketHelperBean.initializeServerChannel(any(Selector.class))).thenReturn(serverChannelMock);

        Set<SelectionKey> keys = getSelectKey(SelectionKey.OP_ACCEPT);
        when(selectorMock.selectedKeys()).thenReturn(keys);

        spiedUnit.run();

        verify(spiedUnit, times(1)).processNewClientConnection(selectorCaptor.capture(), channelCaptor.capture());
        verify(spiedUnit, times(0)).proccessNewClientMessage(any(SocketChannelDelegate.class));

        assertSame(channel, channelCaptor.getValue());
    }

    @Test
    public void testRunNewMessage() throws IOException {
        final ArgumentCaptor<Selector> selectorCaptor = ArgumentCaptor.forClass(Selector.class);
        final ArgumentCaptor<SocketChannelDelegate> channelCaptor = ArgumentCaptor.forClass(SocketChannelDelegate.class);

        final SocketChannelAccepter spiedUnit = spy(unit);

        final Selector selectorMock = Mockito.mock(Selector.class);
        final ServerSocketChannel serverChannelMock = Mockito.mock(ServerSocketChannel.class);

        when(selectorMock.select()).thenReturn(0);

        doNothing().when(channel).configureBlocking(anyBoolean());
        doNothing().when(channel).register(any(Selector.class), anyInt());

        when(socketHelperBean.selectionKeyChannel(any(SelectionKey.class))).thenReturn(channel);
        when(socketHelperBean.keepRunning()).thenReturn(true, false);
        when(socketHelperBean.openSelector()).thenReturn(selectorMock);
        when(socketHelperBean.initializeServerChannel(any(Selector.class))).thenReturn(serverChannelMock);

        Set<SelectionKey> keys = getSelectKey(SelectionKey.OP_READ);
        when(selectorMock.selectedKeys()).thenReturn(keys);

        spiedUnit.run();

        verify(spiedUnit, times(1)).proccessNewClientMessage(channelCaptor.capture());
        verify(spiedUnit, times(0)).processNewClientConnection(any(Selector.class), any(SocketChannelDelegate.class));
        assertSame(channel, channelCaptor.getValue());
    }


    private Set<SelectionKey> getSelectKey(int ops) {
        final Set<SelectionKey> set = new HashSet<>();
        final SelectionKey key = Mockito.mock(SelectionKey.class);
        when(key.readyOps()).thenReturn(ops);
        set.add(key);
        return set;
    }
}
