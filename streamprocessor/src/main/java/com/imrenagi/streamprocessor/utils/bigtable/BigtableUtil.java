package com.imrenagi.streamprocessor.utils.bigtable;

import org.json.JSONObject;

/**
 * Created by chairuni on 04/12/17.
 */
public class BigtableUtil {

    public static byte[] toBytes(Object value, Class expectedDataType) throws Exception {
        return toBytes(value, expectedDataType, null);
    }

    public static byte[] toBytes(Object value, Class expectedDataType, Object defaultValue) throws Exception {

        if (value != JSONObject.NULL && value != null) {
            return toBytesWithoutNullCheck(value, expectedDataType);
        } else if (defaultValue != JSONObject.NULL && defaultValue != null) {
            return toBytesWithoutNullCheck(defaultValue, expectedDataType);
        } else {
            return null;
        }
    }

    private static byte[] toBytesWithoutNullCheck(Object value, Class expectedDataType) {

        return toBytesWithoutNullCheck(value, expectedDataType, false);
    }

    private static byte[] toBytesWithoutNullCheck(Object value, Class expectedDataType, boolean mask) {

        Value valueObject;
        if (expectedDataType == String.class) {
            valueObject = new StringValue(value);
        } else if (expectedDataType == Integer.class) {
            valueObject = new IntegerValue(value);
        } else if (expectedDataType == Long.class) {
            valueObject = new LongValue(value);
        } else if (expectedDataType == Double.class) {
            valueObject = new DoubleValue(value);
        } else if (expectedDataType == Boolean.class) {
            valueObject = new BooleanValue(value);
        } else {
            throw new IllegalArgumentException("This data type for value is not supported: " + value.getClass().toString() + ", value: " + value);
        }

        return valueObject.toBytes(mask);

    }
}
