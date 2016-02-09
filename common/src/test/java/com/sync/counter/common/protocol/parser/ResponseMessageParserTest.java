package com.sync.counter.common.protocol.parser;

import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.ResponseMessageBuilder;
import com.sync.counter.common.protocol.exceptions.WrongMessageException;
import com.sync.counter.common.util.CommonUtil;
import org.junit.Test;

import java.io.IOException;

import static com.sync.counter.common.protocol.ResponseMessage.ResponseType.ok;
import static com.sync.counter.common.protocol.ResponseMessage.ResponseType.wrongMessage;
import static com.sync.counter.common.protocol.ResponseMessage.ResponseType.serverBusy;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ResponseMessageParserTest {

    @Test
    public void testToByteArrayForOk() throws IOException {
        byte[] response = new ResponseMessageParser().toByteArray(createResponseMessage(ok, 987966));
        assertArrayEquals(CommonUtil.createByteSequence(0, 987966), response);
    }

    @Test
    public void testToByteArrayForServerBusy() throws IOException {
        byte[] response = new ResponseMessageParser().toByteArray(createResponseMessage(serverBusy, 332211));
        assertArrayEquals(CommonUtil.createByteSequence(2, 332211), response);
    }

    @Test
    public void testToByteArrayForWrongMessage() throws IOException {
        byte[] response = new ResponseMessageParser().toByteArray(createResponseMessage(wrongMessage, 0));
        assertArrayEquals(CommonUtil.createByteSequence(1, 0), response);
    }

    @Test
    public void testParseOk() throws WrongMessageException, IOException {
        ResponseMessage message = new ResponseMessageParser().parse(CommonUtil.createByteSequence(0, 98799));
        assertEquals(98799, message.getValue().intValue());
        assertEquals(ok, message.getType());
    }

    @Test
    public void testParseWrogmessage() throws WrongMessageException, IOException {
        ResponseMessage message = new ResponseMessageParser().parse(CommonUtil.createByteSequence(1, 654));
        assertEquals(654, message.getValue().intValue());
        assertEquals(wrongMessage, message.getType());
    }

    @Test
    public void testParseServerbusy() throws WrongMessageException, IOException {
        ResponseMessage message = new ResponseMessageParser().parse(CommonUtil.createByteSequence(2, 33553));
        assertEquals(33553, message.getValue().intValue());
        assertEquals(serverBusy, message.getType());
    }

    @Test(expected = WrongMessageException.class)
    public void testParseWrongMessage() throws WrongMessageException, IOException {
        new ResponseMessageParser().parse(CommonUtil.createByteSequence(3, 333));
    }

    private ResponseMessage createResponseMessage(ResponseMessage.ResponseType type, Integer value) {
        ResponseMessageBuilder builder = new ResponseMessageBuilder();
        builder.withType(type);
        if(value != null) {
            builder.withValue(value);
        }
        return builder.build();
    }
}
