package com.imrenagi.streamprocessor.utils.bigtable;

import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by chairuni on 17/01/18.
 */
public class StringValue extends Value {

    private Object value;

    public StringValue (Object value) {

        this.value = value;
    }

    public boolean isValid() {

        return !("".equals(value) || "null".equals(value));
    }

    public byte[] toBytes(boolean mask) {

        if (isValid()) {
            if (mask) {
                return Bytes.toBytes(mask((String) value));
            } else {
                return Bytes.toBytes((String) value);
            }
        } else {
            return null;
        }
    }

    public static String mask(String value) {

        return Hashing
            .sha256().hashString(Hashing.sha256().hashString(value, Charset.forName("UTF-8")).toString(), Charset.forName("UTF-8")).toString();
    }
}