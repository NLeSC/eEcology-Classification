package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MapFeatureExtractor;

import org.joda.time.DateTime;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.google.inject.Inject;

public class MapFeatureExtractorLoader {
    @Inject
    private PathManager pathManager;
    private final String deviceId = "device_info_serial";
    private final String timeStamp = "date_time";

    public void setPathManager(PathManager pathManager) {
        this.pathManager = pathManager;
    }

    public List<FeatureExtractor> getMapFeatureExtractors() {

        String mapFeaturesPath = pathManager.getMapFeaturesPath();
        if (mapFeaturesPath == null || mapFeaturesPath.equals("")) {
            return new LinkedList<>();
        }

        return getMapFeatureExtractorsFromFile(mapFeaturesPath);
    }

    private List<FeatureExtractor> getMapFeatureExtractorsFromFile(String mapFeaturesPath) {
        try {
            CsvMapReader mapReader = new CsvMapReader(new FileReader(mapFeaturesPath), CsvPreference.STANDARD_PREFERENCE);
            List<String> headers = Arrays.asList(mapReader.getHeader(true)).stream().map(h -> h.trim())
                    .collect(Collectors.toList());
            List<String> featureNames = headers.stream().filter(h -> isDeviceId(h) == false && isTimeStamp(h) == false)
                    .collect(Collectors.toList());
            CellProcessor[] processors = headers.stream().map(h -> selectProcessor(h)).toArray(CellProcessor[]::new);
            Map<String, Map<String, Double>> featureMaps = readFeatureExtractorMaps(mapReader, headers, featureNames, processors);
            mapReader.close();
            return featureNames.stream().map(f -> new MapFeatureExtractor(f, featureMaps.get(f))).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new MapFeatureExtractorFileNotFoundException(getFileNotFoundMessage(), e);
        } catch (IOException e) {
            throw new MapFeatureExtractorFileNotReadableException(getFileUnreadableMessage(), e);
        }
    }

    private Map<String, Map<String, Double>> readFeatureExtractorMaps(CsvMapReader mapReader, List<String> headers,
            List<String> featureNames, CellProcessor[] processors) throws IOException {
        Map<String, Map<String, Double>> featureMaps = featureNames.stream().collect(
                Collectors.toMap(f -> f, f -> new HashMap<String, Double>()));
        Map<String, Object> line = readLine(mapReader, headers, processors);
        while (line != null) {
            for (String featureName : featureNames) {
                Map<String, Double> featureMap = featureMaps.get(featureName);
                String key = MapFeatureExtractor.toStringKey((int) line.get(deviceId), (DateTime) line.get(timeStamp));
                Double value = (Double) line.get(featureName);
                featureMap.put(key, value);
            }
            line = readLine(mapReader, headers, processors);
        }
        return featureMaps;
    }

    private Map<String, Object> readLine(CsvMapReader mapReader, List<String> headers, CellProcessor[] processors)
            throws IOException {
        return mapReader.read(headers.toArray(new String[headers.size()]), processors);
    }

    private CellProcessor selectProcessor(String header) {
        CellProcessor processor;
        if (isDeviceId(header)) {
            processor = new ParseInt();
        } else if (isTimeStamp(header)) {
            processor = new JodaTimeCellProcessor(Constants.DATE_TIME_PATTERN_ISO8601);
        } else {
            processor = new ParseDouble();
        }
        return (processor);
    }

    private boolean isTimeStamp(String header) {
        return header.equalsIgnoreCase(timeStamp);
    }

    private boolean isDeviceId(String header) {
        return header.equalsIgnoreCase(deviceId);
    }

    private String getFileUnreadableMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Unable to read the file containing external feature values.");
        message.append("Please specify an accessible path of a csv containing external feature values or leave the setting empty.");
        return message.toString();
    }

    private String getFileNotFoundMessage() {
        StringBuilder message = new StringBuilder();
        message.append("The file containing external feature values was not found. ");
        message.append("Please specify the path of a csv containing external feature values or leave the setting empty.");
        return message.toString();
    }
}
