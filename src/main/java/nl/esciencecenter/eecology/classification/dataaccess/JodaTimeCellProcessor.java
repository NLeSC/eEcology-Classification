package nl.esciencecenter.eecology.classification.dataaccess;

import java.util.LinkedList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

import scala.collection.mutable.StringBuilder;

public class JodaTimeCellProcessor implements CellProcessor {
    private final LinkedList<String> supportedPatterns = new LinkedList<String>();

    /**
     * Adds support for the given date time pattern, while keeping existing support for other patterns.
     *
     * @param dateTimePattern
     * @return the processor for fluent interface style code.
     */
    public JodaTimeCellProcessor withSupportForPattern(String dateTimePattern) {
        supportedPatterns.add(dateTimePattern);
        return this;
    }

    @Override
    public DateTime execute(Object input, CsvContext context) {
        if (input == null) {
            throw new SuperCsvCellProcessorException("Could not parse value as it was null.", context, this);
        }
        if (input instanceof String == false) {
            throw new SuperCsvCellProcessorException("Could not parse value as it is not a String.", context, this);
        }

        return getDateTimeFromStringOrThrowException(context, (String) input);
    }

    private DateTime getDateTimeFromStringOrThrowException(CsvContext context, String inputStr) {
        DateTime result = getDateTimeFromString(inputStr);
        if (result == null) {
            String message = buildExceptionMessage(inputStr, context);
            throw new SuperCsvCellProcessorException(message, context, this);
        }
        return result;
    }

    private DateTime getDateTimeFromString(String inputStr) {
        for (String pattern : supportedPatterns) {
            DateTime result = null;
            try {
                result = getDateTimeWithPattern(inputStr, pattern);
            } catch (IllegalArgumentException e) {
            }
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private DateTime getDateTimeWithPattern(Object input, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern).withZoneUTC();
        return formatter.parseDateTime((String) input);
    }

    private String buildExceptionMessage(String input, CsvContext context) {
        StringBuilder message = new StringBuilder();
        message.append("Could not parse date time using pattern '");
        message.append(String.join("' or '", supportedPatterns));
        message.append("'.");
        return message.toString();
    }
}