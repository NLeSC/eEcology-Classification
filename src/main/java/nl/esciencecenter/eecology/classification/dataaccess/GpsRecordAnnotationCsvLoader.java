package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

        List<GpsRecordAnnotation> results = readGpsFixesFromFile(fileReader);

        return results;
    }

    private List<GpsRecordAnnotation> readGpsFixesFromFile(FileReader fileReader) {
        List<GpsRecordAnnotation> results = new LinkedList<GpsRecordAnnotation>();
        CsvBeanReader beanReader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
        try {
            String[] headers = beanReader.getHeader(true);

            GpsRecordAnnotation gpsFix = null;
            do {
                gpsFix = beanReader.read(GpsRecordAnnotation.class, headers, getProcessors());
                if (gpsFix != null) {
                    results.add(gpsFix);
                }

            } while (gpsFix != null);

            beanReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private CellProcessor[] getProcessors() {
        return new CellProcessor[] { new NotNull(new ParseInt()), // device id
                new JodaTimeCellProcessor(Constants.DATE_TIME_PATTERN_ISO8601), // timestamp
                new NotNull(new ParseInt()) // annotation
        };
    }
}
