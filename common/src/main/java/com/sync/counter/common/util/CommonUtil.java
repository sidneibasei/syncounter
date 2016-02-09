package com.sync.counter.common.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by sidnei on 09/02/16.
 */
public class CommonUtil {

    private CommonUtil() {}

    public static byte[] createByteSequence(Integer ... values) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final DataOutputStream out = new DataOutputStream(bos);
        try {
            for(Integer value : values) {
                out.writeInt(value);
            }
            return bos.toByteArray();
        } finally {
            out.close();
            bos.close();
        }
    }

}
