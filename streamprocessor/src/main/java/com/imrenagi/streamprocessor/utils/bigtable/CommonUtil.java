package com.imrenagi.streamprocessor.utils.bigtable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by chairuni on 05/12/17.
 */
public class CommonUtil {

    public static String getEventDtKey(JSONObject jsonDatum) {

        // Important: the possible event dt keys must be ordered by priority
        List<String> possibleEventDtKeysOrderedList = Arrays.asList(
            "dt",
            "timestamp",
            "systemTimestamp",
            "kafkaPublishTimestamp",
            "kafkaPublishedTimestamp");

        for (String possibleEventDtKey : possibleEventDtKeysOrderedList) {
            if (keyExistsAndHasValidPositiveNonZeroLongValue(jsonDatum, possibleEventDtKey)) {
                return possibleEventDtKey;
            }
        }

        return null;
    }

    public static boolean keyExistsAndHasValidPositiveNonZeroLongValue(JSONObject jsonDatum, String key) {

        if (jsonDatum.has(key)) {

            Object object = jsonDatum.get(key);
            if (object instanceof Long) {
                return (Long) object > 0;
            } else if (object instanceof Integer) {
                return (Integer) object > 0;
            } else if (object instanceof String) {
                try {
                    return (!object.toString().equals("")
                        && !object.toString().equals("null")
                        && Long.parseLong(object.toString()) > 0);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("The value that corresponds to the specified key is a not Long parseable String");
                }
            } else if (object == null || object == JSONObject.NULL) {
                return false;
            } else {
                throw new IllegalArgumentException("The type of the value that corresponds to the specified key is not suitable to be converted to a Long. Got " + object.toString());
            }

        } else {
            return false;
        }
    }

    public static String getStringFromLongPaddedWithZerosInFront(long l, int numDigits) throws IllegalArgumentException {

        String s = Long.toString(l);

        if (s.length() <= numDigits) {
            int numZeros = numDigits - s.length();
            for (int i = 0; i < numZeros; i++) s = "0" + s;
            return s;
        } else {
            throw new IllegalArgumentException("The given long number `l`'s length is greater than the given numDigits");
        }
    }

    public static Long getTimeMillisFromDateString(Object dateString, String dateFormat) throws ParseException {

        if (dateString != null && dateString != JSONObject.NULL) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date parsedDate = sdf.parse((String) dateString);
            return parsedDate.getTime();
        } else {
            return null;
        }
    }
}
