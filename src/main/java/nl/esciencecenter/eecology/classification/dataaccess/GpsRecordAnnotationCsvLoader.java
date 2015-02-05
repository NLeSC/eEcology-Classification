package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordAnnotation;

import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Reads in a csv file from disc and returns a list of GpsFix objects.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class GpsRecordAnnotationCsvLoader implements GpsRecordAnnotationLoader {
    private final String idColumnHeader = "id";
    private final String timeStampColumnHeader = "ts";
    private final String classColumnHeader = "class";

    /**
     * Reads in a csv file from disc and returns a list of GpsFix objects.
     *
     * @param csvFileName
     * @return list of GpsFix objects
     */
    @Override
    public List<GpsRecordAnnotation> load(String csvFileName) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(csvFileName);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Csv file with gps annotation was not found at the specified location ("
                    + csvFileName + ").", e);
        }

        List<GpsRecordAnnotation> results = readGpsFixesFromFile(fileReader, csvFileName);

        return results;
    }

    private List<GpsRecordAnnotation> readGpsFixesFromFile(FileReader fileReader, String csvFileName) {
        List<GpsRecordAnnotation> results = new LinkedList<GpsRecordAnnotation>();
        CsvBeanReader beanReader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
        try {
            String[] headers = beanReader.getHeader(true);
            if (hasIncorrectHeaders(headers)) {
                closeReaders(fileReader, beanReader);
                throw getUnableToReadFileExceptionCauseOfHeaders(csvFileName);
            }
            getGpsFixesFromReader(results, beanReader, headers);
            closeReaders(fileReader, beanReader);
        } catch (IOException | NullPointerException e) {
            throw getUnableToReadFileExceptionCauseUnknown(csvFileName, e);
        }
        return results;
    }

    private UnableToReadGpsFixesFileException getUnableToReadFileExceptionCauseUnknown(String csvFileName, Exception e) {
        StringBuilder message = new StringBuilder();
        message.append("The csv file containing the gps fixes '");
        message.append(csvFileName);
        message.append("' could not be read.");
        UnableToReadGpsFixesFileException unableToReadGpsFixesFileException = new UnableToReadGpsFixesFileException(
                message.toString(), e);
        return unableToReadGpsFixesFileException;
    }

    private UnableToReadGpsFixesFileException getUnableToReadFileExceptionCauseOfHeaders(String csvFileName) {
        StringBuilder message = new StringBuilder();
        message.append("The csv file containing the gps fixes '");
        message.append(csvFileName);
        message.append("' doesn't contain correct column headers.");
        UnableToReadGpsFixesFileException unableToReadGpsFixesFileException = new UnableToReadGpsFixesFileException(
                message.toString(), null);
        return unableToReadGpsFixesFileException;
    }

    private void closeReaders(FileReader fileReader, CsvBeanReader beanReader) throws IOException {
        beanReader.close();
        fileReader.close();
    }

    private boolean hasIncorrectHeaders(String[] headers) {
        boolean hasIdHeader = Arrays.asList(headers).stream().anyMatch(h -> h.equalsIgnoreCase(idColumnHeader));
        boolean hasTimeStampHeader = Arrays.asList(headers).stream().anyMatch(h -> h.equalsIgnoreCase(timeStampColumnHeader));
        boolean hasClassHeader = Arrays.asList(headers).stream().anyMatch(h -> h.equalsIgnoreCase(classColumnHeader));
        boolean hasCorrectHeaders = hasIdHeader && hasTimeStampHeader && hasClassHeader;
        boolean hasIncorrectHeaders = hasCorrectHeaders == false;
        return hasIncorrectHeaders;
    }

    private void getGpsFixesFromReader(List<GpsRecordAnnotation> results, CsvBeanReader beanReader, String[] headers)
            throws IOException {
        GpsRecordAnnotation gpsFix = null;
        do {
            gpsFix = beanReader.read(GpsRecordAnnotation.class, headers, getProcessors());
            if (gpsFix != null) {
                results.add(gpsFix);
            }

        } while (gpsFix != null);
    }

    private CellProcessor[] getProcessors() {
        return new CellProcessor[] { new NotNull(new ParseInt()), // device id
                new JodaTimeCellProcessor(Constants.DATE_TIME_PATTERN_ISO8601), // timestamp
                new NotNull(new ParseInt()) // annotation
        };
    }
}
