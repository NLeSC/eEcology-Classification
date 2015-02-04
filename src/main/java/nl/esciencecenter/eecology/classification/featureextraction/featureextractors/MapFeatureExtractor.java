package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import java.util.Map;

import nl.esciencecenter.eecology.classification.configuration.Constants;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class MapFeatureExtractor extends FeatureExtractor {

    private final String featureName;
    private final Map<String, Double> map;

    public MapFeatureExtractor(String featureName, Map<String, Double> map) {
        this.featureName = featureName;
        this.map = map;
    }

    @Override
    public String getName() {
        return featureName;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        int rows = formattedSegments.getNumberOfRows();
        DoubleMatrix results = new DoubleMatrix(rows, 1);
        for (int i = 0; i < rows; i++) {
            int deviceId = formattedSegments.getDeviceId()[i][0];
            DateTime timeStamp = formattedSegments.getTimeStamp()[i][0];
            Double featureValue = getFeatureValueOrThrowIfNotInMap(deviceId, timeStamp);
            results.put(i, 0, featureValue);
        }
        return results;
    }

    private Double getFeatureValueOrThrowIfNotInMap(int deviceId, DateTime timeStamp) {
        String key = toStringKey(deviceId, timeStamp);
        if (map.containsKey(key)) {
            Double featureValue = map.get(key);
            return featureValue;
        }
        String message = getExceptionMessage(deviceId, timeStamp);
        throw new UnknownKeyException(message.toString());
    }

    private String getExceptionMessage(int deviceId, DateTime timeStamp) {
        StringBuilder message = new StringBuilder();
        message.append("Externally loaded feature extractor '");
        message.append(featureName);
        message.append("' could not find a feature value for instance with device id '");
        message.append(deviceId);
        message.append("' and timeStamp '");
        message.append(toString(timeStamp));
        message.append("'. Please make sure the file defining the values of this feature contains an entry for this id and timestamp.");
        return message.toString();
    }

    /**
     * Returns the string that combines the id and time stamp, used as a key to map to the feature value.
     *
     * @param id
     * @param timeStamp
     * @return
     */
    public static String toStringKey(int id, DateTime timeStamp) {
        return "" + id + "_" + toString(timeStamp);
    }

    private static String toString(DateTime timeStamp) {
        return timeStamp.withZone(DateTimeZone.UTC).toString(Constants.DATE_TIME_PATTERN_ISO8601);
    }
}
