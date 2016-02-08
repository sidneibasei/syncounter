package com.sync.counter.common.protocol.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sync.counter.common.protocol.CounterMessageRequest;
import com.sync.counter.common.protocol.CounterMessageRequest.RequestType;
import com.sync.counter.common.protocol.WrongMessageException;

public class CounterRequestParser {

	public byte[] toByteArray(CounterMessageRequest reponse) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bos);
        try {
            out.writeInt(reponse.getType().getCode());
            Integer value = 0;
            if(reponse.getType().equals(RequestType.dec) || reponse.getType().equals(RequestType.inc) ) {
            	value = reponse.getValue() != null ? reponse.getValue() : 1;
            }
            out.writeInt(value);
            return bos.toByteArray();
        } finally {
            out.close();
            bos.close();
        }
	}
	
	public CounterMessageRequest parse(byte[] bytes) throws WrongMessageException {
		final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        final DataInputStream in = new DataInputStream(bis);
        try {
        	int code = in.readInt();
        	RequestType type = CounterMessageRequest.RequestType.findByCode(code);
            Integer value = null;
            if (type != null) {
                value = in.readInt();
                return  new CounterMessageRequest(type, value);
            }
        } catch(IOException ex) {
        	throw new WrongMessageException();
        }
        finally {
        	try {
	            in.close();
	            bis.close();
        	}catch(IOException e) {}
        }
        throw new WrongMessageException();
	}
}
