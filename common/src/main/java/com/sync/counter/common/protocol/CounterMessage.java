package com.sync.counter.common.protocol;

import java.io.*;

/**
 * Created by sidnei on 04/02/16.
 */
public class CounterMessage {

    public enum MessageType {
        get(0x01),
        inc(0x02),
        dec(0x03);

        private final Integer code;

        MessageType(Integer code) {
            this.code = code;
        }

        public boolean canHaveArgument() {
            return this.equals(inc) || this.equals(dec);
        }

        public Integer getCode() {
            return this.code;
        }

        public static MessageType findByCode(Integer code) {
            for(MessageType type : MessageType.values()) {
                if(type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }

    private MessageType type;
    private Integer value;

    public CounterMessage(MessageType type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public MessageType getType() {
        return type;
    }

    public byte[] getMessage() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bos);

        try {
            out.writeInt(getType().getCode().intValue());
            if (value != null && type.canHaveArgument()) {
                out.writeInt(getValue().intValue());
            }
            return bos.toByteArray();
        } finally {
            out.close();
            bos.close();
        }

    }

    public void readMessage(byte[] bytes) throws IOException {
        final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        final DataInputStream in = new DataInputStream(bis);
        try {
            MessageType type = MessageType.findByCode(in.readInt());
            if (type != null) {
                this.type = type;
                if (type.canHaveArgument()) {
                    this.value = in.readInt();
                }
            }
        } finally {
            in.close();
            bis.close();
        }
    }
}
