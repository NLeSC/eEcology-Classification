package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanAbsDerXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoisePerAbsDerXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoiseXFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class NoisePerAbsDerXFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void extractFeatures_onePointPerSequence_result0() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1 } });
        y = new DoubleMatrix(new double[][] { { 1 } });
        z = new DoubleMatrix(new double[][] { { 1 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(0, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_2pointsPerSequence_result0() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1, 3 } });
        y = new DoubleMatrix(new double[][] { { 1, 1 } });
        z = new DoubleMatrix(new double[][] { { 1, 1 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(0, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_3points_correctResult() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 7, 3, 5 } });
        y = new DoubleMatrix(new double[][] { { 1, 1, 1 } });
        z = new DoubleMatrix(new double[][] { { 1, 1, 1 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(1, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_zeroDerivative_isNotNan() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1, 1, 1 } });
        y = new DoubleMatrix(new double[][] { { 1, 1, 1 } });
        z = new DoubleMatrix(new double[][] { { 1, 1, 1 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert
        assertTrue(Double.isNaN(features.get(0, 0)) == false);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new NoisePerAbsDerXFeatureExtractor(new NoiseXFeatureExtractor(), new MeanAbsDerXFeatureExtractor());
    }
}
