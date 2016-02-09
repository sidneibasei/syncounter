package com.sync.counter.common.protocol;

import com.sync.counter.common.protocol.exceptions.WrongMessageException;

public class RequestMessage extends Message<RequestMessage.RequestType, Integer>{

	RequestMessage(RequestType type, Integer value) {
		super(type, value);
	}

	public enum RequestType {
		get(0xFFFF), inc(0xFFFE), dec(0xFFFD);

		private final Integer code;

		RequestType(Integer code) {
			this.code = code;
		}

		public boolean canHaveArgument() {
			return this.equals(inc) || this.equals(dec);
		}

		public Integer getCode() {
			return this.code;
		}

		public static RequestType findByCode(Integer code) throws WrongMessageException {
			for (RequestType type : RequestType.values()) {
				if (type.getCode().equals(code)) {
					return type;
				}
			}
			throw new WrongMessageException();
		}
	}

}
