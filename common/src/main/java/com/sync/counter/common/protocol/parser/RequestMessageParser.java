package com.sync.counter.common.protocol.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sync.counter.common.protocol.RequestMessage;
import com.sync.counter.common.protocol.RequestMessage.RequestType;
import com.sync.counter.common.protocol.RequestMessageBuilder;
import com.sync.counter.common.protocol.exceptions.WrongMessageException;

public class RequestMessageParser {

	public byte[] toByteArray(RequestMessage reponse) throws IOException {
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
	
	public RequestMessage parse(byte[] bytes) throws WrongMessageException {
		final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        final DataInputStream in = new DataInputStream(bis);
        try {
        	int code = in.readInt();
        	RequestType type = RequestMessage.RequestType.findByCode(code);
            Integer value = null;
            if (type != null) {
                value = in.readInt();
                return new RequestMessageBuilder().withType(type).withValue(value).build();
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
