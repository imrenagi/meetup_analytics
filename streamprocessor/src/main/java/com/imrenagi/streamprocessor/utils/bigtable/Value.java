package com.imrenagi.streamprocessor.utils.bigtable;

/**
 * Created by chairuni on 17/01/18.
 */
public abstract class Value {

    public byte[] toBytes() { return toBytes(false); }
    public abstract byte[] toBytes(boolean mask);
    // TODO make validity check a must for all class implementing Value
    // TODO find a way to standardize the masking algorithm for all class implementing Value
}
