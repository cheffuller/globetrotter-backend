package com.revature.globetrotters.util;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateArgumentConverter implements ArgumentConverter {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof String)) {
            throw new ArgumentConversionException(source + " is not a string");
        }
        String sourceString = (String) source;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            return sdf.parse(sourceString);
        } catch (ParseException e) {
            throw new ArgumentConversionException("Failed to parse date: " + sourceString, e);
        }
    }
}
