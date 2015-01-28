package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordDto;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.util.CsvContext;

/**
 * Reads in a csv file from disc and returns a list of GpsFix objects.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class GpsRecordDtoCsvLoader {
    /**
     * Reads in a csv file from disc and returns a list of GpsFix objects.
     *
     * @param csvFileName
     * @return list of GpsFix objects
     */
    public List<GpsRecordDto> load(String csvFileName) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(csvFileName);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Csv file with gps records was not found at the specified location ("
                    + csvFileName + ").", e);
        }

        List<GpsRecordDto> results = readGpsFixesFromFile(fileReader);

        return results;
    }

    private List<GpsRecordDto> readGpsFixesFromFile(FileReader fileReader) {
        List<GpsRecordDto> results = new LinkedList<GpsRecordDto>();
        CsvBeanReader beanReader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
        try {
            String[] headers = beanReader.getHeader(true);

            GpsRecordDto gpsRecording = null;
            do {
                gpsRecording = beanReader.read(GpsRecordDto.class, headers, getProcessors());
                if (gpsRecording != null) {
                    results.add(gpsRecording);
                }

            } while (gpsRecording != null);

            beanReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    private CellProcessor[] getProcessors() {
        return new CellProcessor[] { new NotNull(new ParseInt()), // device_info_serial
                getJodaTimeCellProcessor(), // date_time
                new NotNull(new ParseDouble()), // latitude
                new NotNull(new ParseDouble()), // longitude
                new NotNull(new ParseInt()), // altitude
                new Optional(new ParseInt()), // pressure
                new Optional(new ParseDouble()), // temperature
                new Optional(new ParseInt()), // satellites_used
                new NotNull(new ParseDouble()), // gps_fixtime
                new NotNull(new ParseDouble()), // positiondop
                new Optional(new ParseDouble()), // h_accuracy
                new Optional(new ParseDouble()), // v_accuracy
                new Optional(new ParseDouble()), // x_speed
                new Optional(new ParseDouble()), // y_speed
                new Optional(new ParseDouble()), // z_speed
                new Optional(new ParseDouble()), // speed_accuracy
                new NotNull(), // location
                new NotNull(new ParseInt()), // userflag
        };
    }

    private CellProcessor getJodaTimeCellProcessor() {
        CellProcessor jodaTimeProcessor = new CellProcessor() {
            @Override
            public DateTime execute(Object input, CsvContext context) {
                if (input == null) {
                    throw new SuperCsvCellProcessorException("Could not parse value as it was null.", context, this);
                }
                if (input instanceof String == false) {
                    throw new SuperCsvCellProcessorException("Could not parse value as it is not a String.", context, this);
                }

                DateTimeFormatter formatter = DateTimeFormat.forPattern(Constants.DATE_TIME_PATTERN_POSTGRES).withOffsetParsed()
                        .withZone(DateTimeZone.UTC);
                return formatter.parseDateTime((String) input);
            }
        };
        return jodaTimeProcessor;
    }
}
