package com.revature.globetrotters.util;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DateArgumentConverter implements ArgumentConverter {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof String)) {
            throw new ArgumentConversionException(source + " is not a string");
        }
        String sourceString = (String) source;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            return LocalDateTime.parse(sourceString, formatter);
        } catch (DateTimeParseException e) {
            throw new ArgumentConversionException("Failed to parse date: " + sourceString, e);
        }
    }

    public static LocalDateTime convertToLocalDateTime(String source) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDateTime.parse(source, formatter);
    }
}
