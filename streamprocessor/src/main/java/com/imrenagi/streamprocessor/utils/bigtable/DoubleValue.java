package com.imrenagi.streamprocessor.utils.bigtable;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by chairuni on 17/01/18.
 */
public class DoubleValue extends Value {

    private Object value;

    public DoubleValue (Object value) {

        this.value = value;
    }

    public byte[] toBytes(boolean mask) {

        Double doubleVal;
        if (value instanceof Long) {
            doubleVal = ((Long) value).doubleValue();
        } else if (value instanceof Integer) {
            doubleVal = ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            doubleVal = (Double) value;
        } else {
            throw new IllegalArgumentException("This data type for Long value is not supported: " + value.getClass().toString() + ", value: " + value);
        }
        return Bytes.toBytes(doubleVal);
    }
}
