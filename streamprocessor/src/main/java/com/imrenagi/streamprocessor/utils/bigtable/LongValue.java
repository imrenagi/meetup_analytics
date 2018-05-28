package com.imrenagi.streamprocessor.utils.bigtable;

import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by chairuni on 17/01/18.
 */
public class LongValue extends Value {

    private Object value;

    public LongValue (Object value) {

        this.value = value;
    }

    public byte[] toBytes(boolean mask) {

        Long longValue;
        if (value instanceof Long) {
            longValue = (Long) value;
        } else if (value instanceof Integer){
            longValue = ((Integer) value).longValue();
        } else if (value instanceof String) {
            try {
                longValue = (Long.parseLong((String) value));
            } catch (NumberFormatException e) {
                throw new NumberFormatException("This String cannot be parsed to Long: " + value);
            }
        } else {
            throw new IllegalArgumentException("This data type for Long value is not supported: " + value.getClass().toString() + ", value: " + value);
        }

        if (mask) {
            return Bytes.toBytes(mask(longValue));
        } else {
            return Bytes.toBytes(longValue);
        }
    }

    public static String mask(Long value) {

        return Hashing.sha256().hashString(Hashing.sha256().hashLong(value).toString(), Charset.forName("UTF-8")).toString();
    }
}
