package com.sync.counter.common.protocol.builder;

import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.RequestMessageBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestMessageBuilderTest {

    @Test
    public void testCreateRequestMessage() {
        final RequestMessage message = new RequestMessageBuilder()
                .withType(RequestMessage.RequestType.inc)
                .withValue(654).build();

        assertEquals(654,message.getValue().intValue());
        assertEquals(0xFFFE, message.getType().getCode().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequestMessageWithNullValue() {
        new RequestMessageBuilder()
                .withType(RequestMessage.RequestType.inc)
                .withValue(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequestMessageWithNullType() {
        new RequestMessageBuilder()
                .withType(null)
                .withValue(6546).build();

    }

}
