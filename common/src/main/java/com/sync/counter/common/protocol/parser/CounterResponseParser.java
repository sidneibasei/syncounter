package com.sync.counter.common.protocol.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sync.counter.common.protocol.CounterMessageResponse;
import com.sync.counter.common.protocol.CounterMessageResponse.ResponseType;

public class CounterResponseParser {

	public byte[] toByteArray(CounterMessageResponse reponse) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bos);
        try {
            out.writeInt(reponse.getType().ordinal());
            Integer value = reponse.getValue() != null ? reponse.getValue() : 0;
            out.writeInt(value);
            return bos.toByteArray();
        } finally {
            out.close();
            bos.close();
        }
	}
	
	public CounterMessageResponse parse(byte[] bytes) throws IOException {
		final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        final DataInputStream in = new DataInputStream(bis);
        try {
        	int responseType = in.readInt();
        	if(responseType >= CounterMessageResponse.ResponseType.values().length) {
        		return null;
        	}
        	ResponseType type = CounterMessageResponse.ResponseType.values()[responseType];
            Integer value = null;
            if (type != null) {
                value = in.readInt();
                return  new CounterMessageResponse(type, value);
            }
        } finally {
            in.close();
            bis.close();
        }
        return null;
	}
}
