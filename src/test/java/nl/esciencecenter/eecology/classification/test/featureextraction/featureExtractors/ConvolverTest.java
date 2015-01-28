package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.Convolver;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class ConvolverTest {
    private Convolver convolver;
    private final double errorMargin = 0.0001;

    @Test
    public void convolve_emptyWithEmpty_returnEmpty() {
        // Arrange
        DoubleMatrix empty = new DoubleMatrix(1, 0);

        // Act        
        DoubleMatrix result = convolver.convolve(empty, empty);

        // Assert        
        assertEquals(0, result.columns);
    }

    @Test
    public void convolve_2SimpleVectorsEvenB_correctResult() {
        // Arrange
        DoubleMatrix a = new DoubleMatrix(new double[][] { { 0, 0, 2 } });
        DoubleMatrix b = new DoubleMatrix(new double[][] { { 1, 0.1 } });
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0, 2, 0.2 } });

        // Act        
        DoubleMatrix result = convolver.convolve(a, b);

        // Assert        
        assertEquals(0, result.sub(expected).norm2(), errorMargin);
    }

    @Test
    public void convolve_2SimpleVectorsOddB_correctResult() {
        // Arrange
        DoubleMatrix a = new DoubleMatrix(new double[][] { { 0, 0, 2 } });
        DoubleMatrix b = new DoubleMatrix(new double[][] { { 0.1, 1, 0.01 } });
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0, 0.2, 2 } });

        // Act        
        DoubleMatrix result = convolver.convolve(a, b);

        // Assert        
        assertEquals(0, result.sub(expected).norm2(), errorMargin);
    }

    @Test
    public void convolve_2vectors_correctResult() {
        // Arrange
        DoubleMatrix a = new DoubleMatrix(new double[][] { { 1, 0, 0, 3, 2, 0, 1, 0, 0 } });
        DoubleMatrix b = new DoubleMatrix(new double[][] { { 0.2000, 0.5000, 0.3000 } });
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0.5, 0.3, 0.6, 1.9, 1.9, 0.8, 0.5, 0.3, 0 } });

        // Act        
        DoubleMatrix result = convolver.convolve(a, b);

        // Assert        
        assertEquals(0, result.sub(expected).norm2(), errorMargin);
    }

    @Test
    public void convolve_2vectorsEvenb_correctResult() {
        // Arrange
        DoubleMatrix a = new DoubleMatrix(new double[][] { { 1, 0, 0, 3, 2, 0, 1, 0, 0 } });
        DoubleMatrix b = new DoubleMatrix(new double[][] { { 1, 0.10 } });
        DoubleMatrix expected = new DoubleMatrix(new double[][] { { 0.1, 0, 3, 2.3, 0.2, 1, 0.1, 0, 0 } });

        // Act        
        DoubleMatrix result = convolver.convolve(a, b);

        // Assert        
        assertEquals(0, result.sub(expected).norm2(), errorMargin);
    }

    @Before
    public void setUp() {
        convolver = new Convolver();
    }
}
