package com.sync.counter.common.protocol;

public class CounterMessageResponse {

	public enum ResponseType {
		ok, 
		wrongMessage, 
		timedout, 
		serverBusy
	}

	private ResponseType type;
	private Integer value;
	
	public CounterMessageResponse(ResponseType type, Integer value) {
		this.type = type;
		this.value = value;
	}

	public ResponseType getType() {
		return type;
	}

	public void setType(ResponseType type) {
		this.type = type;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Boolean isOk() {
		return ResponseType.ok.equals(type);
	}

	public String getErrorMessage() {
		switch (type) {
		case serverBusy:
			return String.format("Server is busy. %d messages on queue right now! Sorry :/", value);
		case timedout:
			return String.format("Server isn't available");
		case wrongMessage:
			return "Wrong message";
		default:
			return "undefined error";
		}
	}

}
