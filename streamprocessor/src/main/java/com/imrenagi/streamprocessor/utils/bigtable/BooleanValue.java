package com.imrenagi.streamprocessor.utils.bigtable;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Created by chairuni on 17/01/18.
 */
public class BooleanValue extends Value {

    private Object value;

    public BooleanValue (Object value) {

        this.value = value;
    }

    public byte[] toBytes(boolean mask) {

        // TODO differentiate the code for masking. Simply no use case for now.
        return Bytes.toBytes((Boolean) value);
    }
}