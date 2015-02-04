package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FirstYFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class FirstYFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void extractFeatures_1emptyInput_OutputZeroVector1() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = new DoubleMatrix(1, 0);
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);
        // Assert
        assertEquals(1, output.columns);
    }

    @Test
    public void extractFeatures_1zeroVectorInput_zeroVectorOutput() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = new DoubleMatrix(1, 1);
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0 } });
        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);
        // Assert
        double error = output.sub(expected).norm2();
        assertTrue(error < errorMargin);
    }

    @Test
    public void extractFeatures_1Vector_correctOutput() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1 } });
        y = new DoubleMatrix(new double[][] { { 2 } });
        z = new DoubleMatrix(new double[][] { { 3 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 2 } });
        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);
        // Assert
        double error = output.sub(expected).norm2();
        assertTrue(error < errorMargin);
    }

    @Test
    public void extractFeatures_2SequenceVectors_correctOutput() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 0, 2 }, { 3, 5 } });
        y = new DoubleMatrix(new double[][] { { 1, 3 }, { 4, 6 } });
        z = new DoubleMatrix(new double[][] { { 2, 4 }, { 5, 7 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 1 }, { 4 } });
        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);
        // Assert
        double error = output.sub(expected).norm2();
        assertTrue(error < errorMargin);
    }

    @Before
    public void setUp() {
        featureExtractor = new FirstYFeatureExtractor();
    }
}
