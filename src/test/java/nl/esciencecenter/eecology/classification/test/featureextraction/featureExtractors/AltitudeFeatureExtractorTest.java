package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.AltitudeFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class AltitudeFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void extractFeatures_multipleGpsRecords_returnsMiddleAltitude() {
        // Arrange
        int expected = 997;
        DoubleMatrix x = new DoubleMatrix(1, 0);
        DoubleMatrix a = new DoubleMatrix(1, 3);
        DoubleMatrix altitude = new DoubleMatrix(new double[][] { { 0, expected, 0 } });
        DateTime[][] t = new DateTime[1][3];
        FormattedSegments formattedSegments = new FormattedSegments(x, x, x, a, a, a, altitude, t);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals(expected, features.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() {
        featureExtractor = new AltitudeFeatureExtractor();
    }

}
