package nl.esciencecenter.eecology.classification.test.dataaccess;

import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.dataaccess.JodaTimeCellProcessor;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.junit.Test;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class JodaTimeCellProcessorTest {
    private final String postGresFormattedInput = "2013-04-19 01:27:59";
    private final ReadableInstant testDateTime = new DateTime(2013, 04, 19, 1, 27, 59, DateTimeZone.UTC);
    private final CsvContext context = new CsvContext(0, 0, 0);

    @Test(expected = SuperCsvCellProcessorException.class)
    public void execute_noPattern_throwException() {
        // Arrange
        JodaTimeCellProcessor jodaTimeCellProcessor = new JodaTimeCellProcessor();

        // Act
        jodaTimeCellProcessor.execute(postGresFormattedInput, context);

        // Assert
    }

    @Test(expected = SuperCsvCellProcessorException.class)
    public void execute_emptyInput_throwException() {
        // Arrange
        JodaTimeCellProcessor jodaTimeCellProcessor = new JodaTimeCellProcessor();

        // Act
        jodaTimeCellProcessor.execute("", context);

        // Assert
    }

    @Test
    public void execute_withPostGressPatternAndInput_returnCorrectDateTime() {
        // Arrange
        JodaTimeCellProcessor jodaTimeCellProcessor = new JodaTimeCellProcessor()
                .withSupportForPattern(Constants.DATE_TIME_PATTERN_POSTGRES);

        // Act
        DateTime result = jodaTimeCellProcessor.execute(postGresFormattedInput, context);

        // Assert
        assertTrue(result.isEqual(testDateTime));
    }

    @Test
    public void execute_withPostGressInputAndMultiplePatterns_returnCorrectDateTime() {
        // Arrange
        JodaTimeCellProcessor jodaTimeCellProcessor = new JodaTimeCellProcessor().withSupportForPattern(
                Constants.DATE_TIME_PATTERN_POSTGRES).withSupportForPattern(Constants.DATE_TIME_PATTERN_ISO8601);

        // Act
        DateTime result = jodaTimeCellProcessor.execute(postGresFormattedInput, context);

        // Assert
        assertTrue(result.isEqual(testDateTime));
    }

    @Test(expected = SuperCsvCellProcessorException.class)
    public void execute_withInvalidInputAndMultiplePatterns_returnCorrectDateTime() {
        // Arrange
        JodaTimeCellProcessor jodaTimeCellProcessor = new JodaTimeCellProcessor().withSupportForPattern(
                Constants.DATE_TIME_PATTERN_POSTGRES).withSupportForPattern(Constants.DATE_TIME_PATTERN_ISO8601);

        // Act
        DateTime result = jodaTimeCellProcessor.execute("string not in an actual time format", context);

        // Assert
        assertTrue(result.isEqual(testDateTime));
    }

}
