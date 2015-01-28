package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.GpsSpeedFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class GpsSpeedFeatureExtractorTest extends FeatureExtractorTest {

    @Test
    public void extractFeatures_speedIs5_outputIs5() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = new DoubleMatrix(1, 0);
        gpsSpeed = new DoubleMatrix(new double[][] { new double[] { 5 } });
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);
        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);
        // Assert
        assertEquals(5.0, output.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_multipleGpsRecords_returnsMiddleGpsSpeed() {
        // Arrange
        int expected = 53;
        DoubleMatrix x = new DoubleMatrix(1, 0);
        DoubleMatrix a = new DoubleMatrix(1, 3);
        DoubleMatrix gpsSpeed = new DoubleMatrix(new double[][] { { 0, expected, 0 } });
        DateTime[][] t = new DateTime[1][3];
        FormattedSegments formattedSegments = new FormattedSegments(x, x, x, gpsSpeed, a, a, a, t);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new GpsSpeedFeatureExtractor();
    }
}
