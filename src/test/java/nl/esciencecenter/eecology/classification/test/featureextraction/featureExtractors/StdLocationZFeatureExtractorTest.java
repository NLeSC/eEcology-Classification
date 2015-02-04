package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdLocationZFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class StdLocationZFeatureExtractorTest extends FeatureExtractorTest {
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
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0 } });

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        double error = output.sub(expected).norm2();
        assertTrue(error < errorMargin);
    }

    @Test
    public void extractFeatures_1SequenceVector_correctOutput() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 0, 2 } });
        y = new DoubleMatrix(new double[][] { { 1, 5 } });
        z = new DoubleMatrix(new double[][] { { 6, 8 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { Math.sqrt(2 / 1) } });

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
        x = new DoubleMatrix(new double[][] { { 0, 2 }, { 8, 12 } });
        y = new DoubleMatrix(new double[][] { { 1, 5 }, { 1, 3 } });
        z = new DoubleMatrix(new double[][] { { 6, 8 }, { 5, 7 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { Math.sqrt(2 / 1) }, { Math.sqrt(2 / 1) } });

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        double error = output.sub(expected).norm2();
        assertTrue(error < errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new StdLocationZFeatureExtractor(new StdExtractor());
    }
}
