package com.sync.counter.common.protocol.parser;

import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.RequestMessageBuilder;
import com.sync.counter.common.protocol.exceptions.WrongMessageException;
import com.sync.counter.common.util.CommonUtil;
import org.junit.Test;

import java.io.IOException;

import static com.sync.counter.common.protocol.RequestMessage.RequestType;
import static com.sync.counter.common.protocol.RequestMessage.RequestType.inc;
import static com.sync.counter.common.protocol.RequestMessage.RequestType.dec;
import static com.sync.counter.common.protocol.RequestMessage.RequestType.get;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by sidnei on 09/02/16.
 */
public class RequestMessageParserTest {

    @Test
    public void testToByteArrayForInc() throws IOException {
        byte[] response = new RequestMessageParser().toByteArray(createRequestMessage(inc, 665));
        assertArrayEquals(CommonUtil.createByteSequence(0xFFFE, 665), response);
    }

    @Test
    public void testToByteArrayForDec() throws IOException {
        byte[] response = new RequestMessageParser().toByteArray(createRequestMessage(dec, 9879));
        assertArrayEquals(CommonUtil.createByteSequence(0xFFFD, 9879), response);
    }

    @Test
    public void testToByteArrayForGet() throws IOException {
        byte[] response = new RequestMessageParser().toByteArray(createRequestMessage(get, 0));
        assertArrayEquals(CommonUtil.createByteSequence(0xFFFF, 0), response);
    }


    @Test
    public void testToByteArrayForIncWithNullValue() throws IOException {
        byte[] response = new RequestMessageParser().toByteArray(createRequestMessage(inc, null));
        assertArrayEquals(CommonUtil.createByteSequence(0xFFFE, 1), response);
    }

    @Test
    public void testToByteArrayForDecWithNullValue() throws IOException {
        byte[] response = new RequestMessageParser().toByteArray(createRequestMessage(dec, null));
        assertArrayEquals(CommonUtil.createByteSequence(0xFFFD, 1), response);
    }

    @Test
    public void testToByteArrayForGetWithNullValue() throws IOException {
        byte[] response = new RequestMessageParser().toByteArray(createRequestMessage(get, null));
        assertArrayEquals(CommonUtil.createByteSequence(0xFFFF, 0), response);
    }

    @Test
    public void testParseDec() throws WrongMessageException, IOException {
        RequestMessage message = new RequestMessageParser().parse(CommonUtil.createByteSequence(dec.getCode(), 6));
        assertEquals(6, message.getValue().intValue());
        assertEquals(dec, message.getType());
    }

    @Test
    public void testParseInc() throws WrongMessageException, IOException {
        RequestMessage message = new RequestMessageParser().parse(CommonUtil.createByteSequence(inc.getCode(), 654));
        assertEquals(654, message.getValue().intValue());
        assertEquals(inc, message.getType());
    }

    @Test
    public void testParseGet() throws WrongMessageException, IOException {
        RequestMessage message = new RequestMessageParser().parse(CommonUtil.createByteSequence(get.getCode(), 333));
        assertEquals(333, message.getValue().intValue());
        assertEquals(get, message.getType());
    }

    @Test(expected = WrongMessageException.class)
    public void testParseWrongMessage() throws WrongMessageException, IOException {
        new RequestMessageParser().parse(CommonUtil.createByteSequence(0x0fdff, 333));
    }

    private RequestMessage createRequestMessage(RequestType type, Integer value) {
        RequestMessageBuilder builder = new RequestMessageBuilder();
        builder.withType(type);
        if(value != null) {
            builder.withValue(value);
        }
        return builder.build();
    }
}
