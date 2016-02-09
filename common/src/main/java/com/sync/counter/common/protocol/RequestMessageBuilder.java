package com.sync.counter.common.protocol;

import com.sync.counter.common.protocol.RequestMessage.RequestType;


public class RequestMessageBuilder extends MessageBuilder<RequestType, Integer, RequestMessage>{

	@Override
	public RequestMessage build() {
		return new RequestMessage(type, value);
	}
}
