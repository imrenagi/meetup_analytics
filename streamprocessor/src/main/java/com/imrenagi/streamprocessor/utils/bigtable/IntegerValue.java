package com.imrenagi.streamprocessor.utils.bigtable;

import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by chairuni on 17/01/18.
 */
public class IntegerValue extends Value {

    private Object value;

    public IntegerValue (Object value) {

        this.value = value;
    }

    public byte[] toBytes(boolean mask) {

        if (mask) {
            return Bytes.toBytes(mask((Integer) value));
        } else {
            return Bytes.toBytes((Integer) value);
        }
    }

    public static String mask(int value) {

        return Hashing.sha256().hashString(Hashing.sha256().hashInt(value).toString(), Charset.forName("UTF-8")).toString();
    }
}