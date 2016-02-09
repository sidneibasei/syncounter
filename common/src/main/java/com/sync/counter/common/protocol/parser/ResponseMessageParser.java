package com.sync.counter.common.protocol.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sync.counter.common.protocol.ResponseMessage;
import com.sync.counter.common.protocol.ResponseMessage.ResponseType;
import com.sync.counter.common.protocol.ResponseMessageBuilder;
import com.sync.counter.common.protocol.exceptions.WrongMessageException;

public class ResponseMessageParser {

	public byte[] toByteArray(ResponseMessage reponse) throws IOException {
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
	
	public ResponseMessage parse(byte[] bytes) throws WrongMessageException {
		final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        final DataInputStream in = new DataInputStream(bis);
        try {
        	int responseType = in.readInt();
        	if(responseType < ResponseMessage.ResponseType.values().length) {

                ResponseType type = ResponseMessage.ResponseType.values()[responseType];
                Integer value = null;
                if (type != null) {
                    value = in.readInt();
                    return new ResponseMessageBuilder().withType(type).withValue(value).build();
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
                bis.close();
            }catch(IOException e) {}
        }
        throw new WrongMessageException();
	}
}
