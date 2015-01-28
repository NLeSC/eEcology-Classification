package nl.esciencecenter.eecology.classification.dataaccess;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class JodaTimeCellProcessor implements CellProcessor {
    private final String dateTimePattern;

    public JodaTimeCellProcessor(String dateTimePattern) {
        this.dateTimePattern = dateTimePattern;
    }

    @Override
    public DateTime execute(Object input, CsvContext context) {
        if (input == null) {
            throw new SuperCsvCellProcessorException("Could not parse value as it was null.", context, this);
        }
        if (input instanceof String == false) {
            throw new SuperCsvCellProcessorException("Could not parse value as it is not a String.", context, this);
        }

        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateTimePattern).withZoneUTC();
        return formatter.parseDateTime((String) input);

    }
}