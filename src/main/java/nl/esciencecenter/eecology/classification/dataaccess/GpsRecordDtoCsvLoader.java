package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.segmentloading.GpsRecordDto;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvException;
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
    private final Map<String, CellProcessor> cellProcessorsByHeader = createCellProcessorsByHeader();

    /**
     * Reads in a csv file from disc and returns a list of GpsFix objects.
     *
     * @param csvFileName
     * @return list of GpsFix objects
     */
    public List<GpsRecordDto> load(String csvFileName) {
        FileReader fileReader = getFileReader(csvFileName);
        return loadGpsRecordsFromFileReader(fileReader);
    }

    private List<GpsRecordDto> loadGpsRecordsFromFileReader(FileReader fileReader) {
        List<GpsRecordDto> results = null;
        try {
            results = getResultsFromFile(fileReader);
        } catch (IOException e) {
            throwGenericGpsLoaderException(e);
        } catch (SuperCsvException e) {
            CsvContext csvContext = e.getCsvContext();
            if (csvContext == null) {
                throwGenericGpsLoaderException(e);
            }
            throwGpsLoaderExceptionWithContext(e, csvContext);
        }
        return results;
    }

    private FileReader getFileReader(String csvFileName) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(csvFileName);
        } catch (FileNotFoundException e) {
            throw new GpsRecordLoadingException("Csv file with gps records was not found at the specified location ("
                    + csvFileName + ").", e);
        }
        return fileReader;
    }

    private List<GpsRecordDto> getResultsFromFile(FileReader fileReader) throws IOException {
        CsvBeanReader beanReader = new CsvBeanReader(fileReader, CsvPreference.STANDARD_PREFERENCE);
        String[] headers = getHeadersFromFile(beanReader);
        CellProcessor[] processors = getProcessorsForHeaders(headers);
        List<GpsRecordDto> results = readRecordsFromFile(beanReader, headers, processors);
        beanReader.close();
        fileReader.close();
        return results;
    }

    private List<GpsRecordDto> readRecordsFromFile(CsvBeanReader beanReader, String[] headers, CellProcessor[] processors)
            throws IOException {
        List<GpsRecordDto> results = new LinkedList<GpsRecordDto>();
        GpsRecordDto gpsRecording = null;
        do {
            gpsRecording = beanReader.read(GpsRecordDto.class, headers, processors);
            if (gpsRecording != null) {
                results.add(gpsRecording);
            }
        } while (gpsRecording != null);
        return results;
    }

    private String[] getHeadersFromFile(CsvBeanReader beanReader) throws IOException {
        Stream<String> allHeadersInFile = Arrays.stream(beanReader.getHeader(true)).map(h -> h.trim());
        Set<String> familiarHeaders = getCellProcessorsByHeader().keySet();
        List<String> familiarHeadersInFile = allHeadersInFile.map(h -> familiarHeaders.contains(h) ? h : null).collect(
                Collectors.toList());
        List<String> missingHeaders = familiarHeaders.stream().filter(f -> familiarHeadersInFile.contains(f) == false)
                .collect(Collectors.toList());
        if (missingHeaders.size() > 0) {
            throwMissingColumnsException(missingHeaders);
        }
        return familiarHeadersInFile.toArray(new String[familiarHeadersInFile.size()]);
    }

    private void throwMissingColumnsException(List<String> missingHeaders) {
        StringBuilder message = new StringBuilder();
        message.append("Could not load csv file containing gps records because the following expected columns were missing: \"");
        message.append(String.join("\", \"", missingHeaders));
        message.append("\"");
        throw new GpsRecordLoadingMissingColumnsException(message.toString(), null);
    }

    private CellProcessor[] getProcessorsForHeaders(String[] headers) {
        return Arrays.stream(headers).map(h -> getCellProcessorsByHeader().get(h)).toArray(CellProcessor[]::new);
    }

    private Map<String, CellProcessor> getCellProcessorsByHeader() {
        if (cellProcessorsByHeader == null) {
            createCellProcessorsByHeader();
        }
        return cellProcessorsByHeader;
    }

    private Map<String, CellProcessor> createCellProcessorsByHeader() {
        Map<String, CellProcessor> cellProcessors = new HashMap<String, CellProcessor>();
        cellProcessors.put("device_info_serial", new NotNull(new ParseInt()));
        cellProcessors.put("date_time", new JodaTimeCellProcessor().withSupportForPattern(Constants.DATE_TIME_PATTERN_POSTGRES));
        cellProcessors.put("latitude", new NotNull(new ParseDouble()));
        cellProcessors.put("longitude", new NotNull(new ParseDouble()));
        cellProcessors.put("altitude", new NotNull(new ParseInt()));
        cellProcessors.put("pressure", new Optional(new ParseInt()));
        cellProcessors.put("temperature", new Optional(new ParseDouble()));
        cellProcessors.put("satellites_used", new Optional(new ParseInt()));
        cellProcessors.put("gps_fixtime", new NotNull(new ParseDouble()));
        cellProcessors.put("positiondop", new NotNull(new ParseDouble()));
        cellProcessors.put("h_accuracy", new Optional(new ParseDouble()));
        cellProcessors.put("v_accuracy", new Optional(new ParseDouble()));
        cellProcessors.put("x_speed", new Optional(new ParseDouble()));
        cellProcessors.put("y_speed", new Optional(new ParseDouble()));
        cellProcessors.put("z_speed", new Optional(new ParseDouble()));
        cellProcessors.put("speed_accuracy", new Optional(new ParseDouble()));
        cellProcessors.put("location", new NotNull());
        cellProcessors.put("userflag", new NotNull(new ParseInt()));
        cellProcessors.put("speed_2d", new Optional(new ParseDouble()));
        cellProcessors.put("speed_3d", new Optional(new ParseDouble()));
        cellProcessors.put("direction", new Optional(new ParseDouble()));
        cellProcessors.put("altitude_agl", new Optional(new ParseDouble()));
        return cellProcessors;
    };

    private void throwGpsLoaderExceptionWithContext(SuperCsvException e, CsvContext csvContext) {
        StringBuilder message = new StringBuilder();
        message.append("Could not load gps records from csv file because of a problem at line ");
        message.append(csvContext.getLineNumber());
        message.append(".");
        throw new GpsRecordLoadingException(message.toString(), e);
    }

    private void throwGenericGpsLoaderException(Exception e) {
        StringBuilder message = new StringBuilder();
        message.append("Could not load gps records from csv file.");
        throw new GpsRecordLoadingException(message.toString(), e);
    }
}
