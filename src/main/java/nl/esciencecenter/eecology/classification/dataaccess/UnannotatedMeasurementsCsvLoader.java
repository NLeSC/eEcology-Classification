package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.segmentloading.IndependentMeasurement;

import org.joda.time.DateTime;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 * Loads measurements from csv files. Can handle multiple csv files. For this, multiple paths can be given separated by commas.
 *
 * @author Christiaan Meijer, NLeSc
 *
 */
public class UnannotatedMeasurementsCsvLoader extends MeasurementsLoader {
    public List<IndependentMeasurement> loadFromSingleSource(String sourcePath) {
        FileReader fileReader = null;
        try {
            String trimmed = sourcePath.trim();
            fileReader = new FileReader(trimmed);
        } catch (FileNotFoundException e) {
            throw new LoadingMeasurementsException(
                    "Csv file with unannotated measurements was not found at the specified location (" + sourcePath + ").", e);
        }

        List<IndependentMeasurement> results = readMeasurementsFromFile(fileReader);

        try {
            fileReader.close();
        } catch (IOException e) {
            throw getRuntimeIoException(e);
        }

        return results;

    }

    private List<IndependentMeasurement> readMeasurementsFromFile(FileReader fileReader) {
        List<IndependentMeasurement> results = new LinkedList<IndependentMeasurement>();
        CsvMapReader mapReader = new CsvMapReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
        try {
            String[] header = mapReader.getHeader(true);
            CellProcessor[] processors = getProcessors();
            Map<String, Object> map = null;
            do {
                map = mapReader.read(header, processors);
                addResultIfValid(results, map);
            } while (map != null);

            mapReader.close();
        } catch (IOException e) {
            throw getRuntimeIoException(e);
        } catch (SuperCsvException e) {
            throw new LoadingMeasurementsException(
                    "The csv file containing the measurements cannot be read because the format is invalid. The reading error occured at line "
                            + e.getCsvContext().getLineNumber() + " column " + e.getCsvContext().getColumnNumber() + ".", e);
        }

        return results;
    }

    private void addResultIfValid(List<IndependentMeasurement> results, Map<String, Object> map) {
        if (map != null) {
            IndependentMeasurement measurement = getUnannotatedMeasurement((double) map.get("x_cal"), (double) map.get("y_cal"),
                    (double) map.get("z_cal"), (DateTime) map.get("date_time"), (int) map.get("device_info_serial"),
                    (double) map.get("speed"), (double) map.get("longitude"), (double) map.get("latitude"),
                    (double) map.get("altitude"));
            int index = (int) map.get("index");
            if (index >= 0) {
                measurement.setIndex(index);
            }
            if (isMeasurementValid(measurement)) {
                results.add(measurement);
            }
        }
    }

    private CellProcessor[] getProcessors() {
        return new CellProcessor[] { new ParseInt(), // device id
                new JodaTimeCellProcessor().withSupportForPattern(Constants.DATE_TIME_PATTERN_POSTGRES), // timestamp
                new ConvertNullTo(Double.NaN, new ParseDouble()), // speed
                new ConvertNullTo(Double.NaN, new ParseDouble()), // longitude
                new ConvertNullTo(Double.NaN, new ParseDouble()), // latitude
                new ConvertNullTo(Double.NaN, new ParseDouble()), // altitude
                new ConvertNullTo(Double.NaN, new ParseDouble()), // tspeed
                new ConvertNullTo(-1, new ParseInt()), // index
                new ConvertNullTo(Double.NaN, new ParseDouble()), // x
                new ConvertNullTo(Double.NaN, new ParseDouble()), // y
                new ConvertNullTo(Double.NaN, new ParseDouble()) // z
        };
    }

    private LoadingMeasurementsException getRuntimeIoException(IOException e) {
        return new LoadingMeasurementsException(
                "An input/output error occured while reading the csv file with unannotated measurements. "
                        + "This indicates a problem with the disk/device the file is stored on.", e);
    }

}
