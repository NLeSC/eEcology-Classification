package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanAbsDerZFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class MeanAbsDerZFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void extractFeatures_ones_result0() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1, 1 } });
        y = new DoubleMatrix(new double[][] { { 1, 1 } });
        z = new DoubleMatrix(new double[][] { { 1, 1 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert        
        assertEquals(0, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_onePointPerSequence_result0() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1 } });
        y = new DoubleMatrix(new double[][] { { 1 } });
        z = new DoubleMatrix(new double[][] { { 1 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert        
        assertEquals(0, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_2pointsPositiveDerivative_correctResult() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1, 1 } });
        y = new DoubleMatrix(new double[][] { { 1, 1 } });
        z = new DoubleMatrix(new double[][] { { 1, 3 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert        
        assertEquals(2, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_2pointsNegativeDerivative_positiveResult() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1, 1 } });
        y = new DoubleMatrix(new double[][] { { 1, 1 } });
        z = new DoubleMatrix(new double[][] { { 7, 3 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert        
        assertEquals(4, features.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_3points_correctResult() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1, 1, 1 } });
        y = new DoubleMatrix(new double[][] { { 1, 1, 1 } });
        z = new DoubleMatrix(new double[][] { { 7, 3, 5 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix features = featureExtractor.extractFeatures(input);

        // Assert        
        assertEquals(3, features.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new MeanAbsDerZFeatureExtractor();
    }
}
