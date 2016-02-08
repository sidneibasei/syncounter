package com.sync.counter.common.protocol;

public class CounterMessageRequest {

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

	private final RequestType type;
	private final Integer value;

	public CounterMessageRequest(RequestType type, Integer value) {
		this.type = type;
		this.value = value;
	}
	
	public RequestType getType() {
		return type;
	}

	public Integer getValue() {
		return value;
	}	
}
