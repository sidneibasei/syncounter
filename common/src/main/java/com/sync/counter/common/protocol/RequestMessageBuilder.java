package com.sync.counter.common.protocol;

import com.sync.counter.common.protocol.CounterMessageRequest.RequestType;


public class RequestMessageBuilder {

	private RequestType type;
	private Integer value;
	
	public RequestMessageBuilder() {
	}
	
	public RequestMessageBuilder withType(RequestType type) {
		this.type = type;
		return this;
	}
	
	public RequestMessageBuilder withValue(Integer value) {
		this.value = value;
		return this;
	}
	
	public CounterMessageRequest build() {
		return new CounterMessageRequest(type, value);
	}
}
