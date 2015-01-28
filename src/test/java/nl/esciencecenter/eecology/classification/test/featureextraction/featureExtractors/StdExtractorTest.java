package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Test;

public class StdExtractorTest {
    private final double errorMargin = 0.0001;

    @Test
    public void extractStd_simpleValues_correctResult() {
        // Arrange
        StdExtractor stdExtractor = new StdExtractor();
        double[] array = { 1, 2, 3 };
        double expected = Math.sqrt((1 + 0 + 1) / (3 - 1));
        DoubleMatrix matrix = new DoubleMatrix(array).transpose();

        // Act
        DoubleMatrix std = stdExtractor.extractStd(matrix);

        // Assert
        assertEquals(expected, std.get(0), errorMargin);
    }

    @Test
    public void extractStd_1Value_return0() {
        // Arrange
        StdExtractor stdExtractor = new StdExtractor();
        double[] array = { 8 };
        double expected = 0;
        DoubleMatrix matrix = new DoubleMatrix(array).transpose();

        // Act
        DoubleMatrix std = stdExtractor.extractStd(matrix);

        // Assert
        assertEquals(expected, std.get(0), errorMargin);
    }

    @Test
    public void extractStd_realisticValues_correctResult() {
        // Arrange
        StdExtractor stdExtractor = new StdExtractor();
        double[] array = { 0.3816, 0.7655, 0.7952, 0.1869, 0.4898, 0.4456, 0.6463, 0.7094, 0.7547, 0.2760 };
        // Matlab code:  std([0.3816, 0.7655, 0.7952, 0.1869, 0.4898, 0.4456, 0.6463, 0.7094, 0.7547, 0.2760]);
        double matlabResult = 0.2193;
        DoubleMatrix matrix = new DoubleMatrix(array).transpose();

        // Act
        DoubleMatrix std = stdExtractor.extractStd(matrix);

        // Assert
        assertEquals(matlabResult, std.get(0), errorMargin);
    }
}
