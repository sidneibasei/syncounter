package com.sync.counter.common.protocol.builder;

import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.ResponseMessageBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResponseMessageBuilderTest {

    @Test
    public void testCreateResponseMessage() {
        final ResponseMessage message = new ResponseMessageBuilder()
                .withType(ResponseMessage.ResponseType.ok)
                .withValue(6666).build();
        assertEquals(6666,message.getValue().intValue());
        assertEquals(0, message.getType().ordinal());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateResponseMessageWithNullValue() {
        new ResponseMessageBuilder()
                .withType(ResponseMessage.ResponseType.wrongMessage)
                .withValue(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateResponseMessageWithNullType() {
        new ResponseMessageBuilder()
                .withType(null)
                .withValue(6546).build();
    }

}
