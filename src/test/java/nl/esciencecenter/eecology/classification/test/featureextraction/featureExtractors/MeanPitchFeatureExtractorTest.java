package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanPitchFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.PitchFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class MeanPitchFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void extractFeatures_1emptyInput_OutputZeroVector3() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = y = z = new DoubleMatrix(1, 0);
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);
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
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0, 0, 0 } });
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
        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 15.5014 } });
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

        x = new DoubleMatrix(new double[][] { { 1, 2 } });
        y = new DoubleMatrix(new double[][] { { 3, 4 } });
        z = new DoubleMatrix(new double[][] { { 5, 6 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);

        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 12.6164 } });
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

        x = new DoubleMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
        y = new DoubleMatrix(new double[][] { { 5, 6 }, { 7, 8 } });
        z = new DoubleMatrix(new double[][] { { 2, 4 }, { 5, 9 } });

        gpsSpeed = new DoubleMatrix(x.rows, 1);

        FormattedSegments input = new FormattedSegments(x, y, z, gpsSpeed);
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 13.0105 }, { 18.8007 } });
        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);
        // Assert
        double error = output.sub(expected).norm2();
        assertTrue(error < errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new MeanPitchFeatureExtractor(new PitchFeatureExtractor(), new MeanExtractor());
    }
}
