package com.sync.counter.common.protocol;

import com.sync.counter.common.protocol.CounterMessageResponse.ResponseType;

public class ResponseMessageBuilder {

	private ResponseType type;
	private Integer value;
	
	public ResponseMessageBuilder() {
	}
	
	public ResponseMessageBuilder withType(ResponseType type) {
		this.type = type;
		return this;
	}
	
	public ResponseMessageBuilder withValue(Integer value) {
		this.value = value;
		return this;
	}
	
	public CounterMessageResponse build() {
		return new CounterMessageResponse(type, value);
	}
}
