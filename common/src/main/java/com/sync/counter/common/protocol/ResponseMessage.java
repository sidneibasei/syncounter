package com.sync.counter.common.protocol;

public class ResponseMessage extends Message<ResponseMessage.ResponseType, Integer> {

	ResponseMessage(ResponseType type, Integer value) {
		super(type, value);
	}

	public enum ResponseType {
		ok, 
		wrongMessage,
		serverBusy
	}

	public boolean isOk() {
		return ResponseType.ok.equals(type);
	}

	public String getErrorMessage() {
		switch (type) {
		case serverBusy:
			return String.format("Server is busy. %d messages on queue right now! Sorry :/", value);
		case wrongMessage:
			return "Wrong message";
		default:
			return "undefined error";
		}
	}

}
