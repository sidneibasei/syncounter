package com.sync.counter.common.protocol;

import com.sync.counter.common.protocol.ResponseMessage.ResponseType;

public class ResponseMessageBuilder extends MessageBuilder<ResponseType, Integer, ResponseMessage> {

	public ResponseMessage build() {
		return new ResponseMessage(type, value);
	}
}
