package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MapFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.UnknownKeyException;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

public class MapFeatureExtractorTest extends FeatureExtractorTest {
    Map<String, Double> map = new HashMap<String, Double>();

    @Test
    public void extractFeatures_variousTimeStamps_correctFeatures() {
        // Arrange
        int constantId = 48;
        int[][] deviceIds = { { constantId }, { constantId } };
        DateTime[][] timeStamps = { { new DateTime(2015, 2, 2, 9, 46, DateTimeZone.UTC) },
                { new DateTime(2015, 2, 2, 9, 47, DateTimeZone.UTC) } };
        double[] features = { 45.6, -12.3 };
        putInMap(timeStamps, deviceIds, features);
        FormattedSegments formattedSegments = createFormattedsegments(timeStamps, deviceIds);

        // Act
        DoubleMatrix extractFeatures = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals("Feature of first instance is incorrect.", features[0], extractFeatures.get(0, 0), errorMargin);
        assertEquals("Feature of second instance is incorrect.", features[1], extractFeatures.get(1, 0), errorMargin);
    }

    @Test
    public void extractFeatures_variousIds_correctFeatures() {
        // Arrange
        DateTime constantDateTime = new DateTime(2015, 2, 2, 9, 46, DateTimeZone.UTC);
        DateTime[][] timeStamps = { { constantDateTime }, { new DateTime(constantDateTime) } };
        int[][] deviceIds = { { 48 }, { 3 } };
        double[] features = { 45.6, -12.3 };
        putInMap(timeStamps, deviceIds, features);
        FormattedSegments formattedSegments = createFormattedsegments(timeStamps, deviceIds);

        // Act
        DoubleMatrix extractFeatures = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals("Feature of first instance is incorrect.", features[0], extractFeatures.get(0, 0), errorMargin);
        assertEquals("Feature of second instance is incorrect.", features[1], extractFeatures.get(1, 0), errorMargin);
    }

    @Test(expected = UnknownKeyException.class)
    public void extractFeatures_keyNotInMap_throwCorrectException() {
        // Arrange
        putInMap(new DateTime[][] {}, new int[][] {}, new double[] {});

        DateTime[][] timeStamps = { { new DateTime(2015, 2, 2, 9, 46, DateTimeZone.UTC) } };
        int[][] deviceIds = { { 48 } };
        FormattedSegments formattedSegments = createFormattedsegments(timeStamps, deviceIds);

        // Act
        featureExtractor.extractFeatures(formattedSegments);
    }

    private void putInMap(DateTime[][] timeStamps, int[][] deviceIds, double[] features) {
        MapFeatureExtractor mapFeatureExtractor = (MapFeatureExtractor) featureExtractor;
        for (int i = 0; i < features.length; i++) {
            map.put(mapFeatureExtractor.toStringKey(deviceIds[i][0], timeStamps[i][0]), features[i]);
        }
    }

    private FormattedSegments createFormattedsegments(DateTime[][] timeStamps, int[][] deviceIds) {
        int rows = timeStamps.length;
        DoubleMatrix x = new DoubleMatrix(rows, 0);
        DoubleMatrix g = new DoubleMatrix(rows, 0);
        FormattedSegments formattedSegments = new FormattedSegments(x, x, x, g, g, g, g, timeStamps, deviceIds);
        return formattedSegments;
    }

    @Before
    public void setUp() {
        MapFeatureExtractor mapFeatureExtractor = new MapFeatureExtractor("testmapfeature", map);
        map.put(mapFeatureExtractor.toStringKey(defaultDeviceId, defaultTestDateTime), 42.3);
        featureExtractor = mapFeatureExtractor;
    }
}
