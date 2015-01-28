package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import junit.framework.TestCase;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.HammingWindowing;

public class HammingWindowingTest extends TestCase {
    // test the Hamming window with odd length
    private final int oddLength = 5;
    private final int evenLength = 6;
    protected HammingWindowing hammingWindowingOdd;
    // test the Hamming window with even length
    protected HammingWindowing hammingWindowingEven;
    protected final double errorMargin = 0.0001;

    // odd length
    public void test_HammingWindowing_OddWindowLength() {
        // Arrange
        double[] output = new double[oddLength];
        // Act
        hammingWindowingOdd.constructWindow(oddLength);
        output = hammingWindowingOdd.GetWindow();
        // Assert
        assertEquals(oddLength, output.length);
    }

    public void test_HammingWIndowing_OddWindowValues() {
        // Arrange
        double[] output = new double[oddLength];
        double[] expected = new double[oddLength];
        expected[0] = 0.08;
        expected[1] = 0.54;
        expected[2] = 1.0;
        expected[3] = 0.54;
        expected[4] = 0.08;

        // Act
        hammingWindowingOdd.constructWindow(oddLength);
        output = hammingWindowingOdd.GetWindow();
        // Assert
        double sumSquaredDistance = 0;
        for (int i = 0; i < oddLength; i++) {
            sumSquaredDistance += Math.pow(output[i] - expected[i], 2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    public void test_HammingWIndowing_1Input_oddSize_correctOutput() {
        // Arrange
        double[] input = new double[oddLength];
        input[0] = 5;
        input[1] = -3.1;
        input[2] = 2.4;
        input[3] = -8;
        input[4] = 0.5;

        double[] expected = new double[oddLength];
        expected[0] = 0.4;
        expected[1] = -1.674;
        expected[2] = 2.4;
        expected[3] = -4.32;
        expected[4] = 0.04;

        // Act
        hammingWindowingOdd.constructWindow(oddLength);
        double[] output = hammingWindowingOdd.doHammingWindowing(input);
        // Assert
        double sumSquaredDistance = 0;
        for (int i = 0; i < input.length; i++) {
            sumSquaredDistance += Math.pow(output[i] - expected[i], 2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    // even length
    public void test_HammingWindowing_EvenWindowLength() {
        // Arrange
        double[] output = new double[evenLength];
        // Act
        hammingWindowingEven.constructWindow(evenLength);
        output = hammingWindowingEven.GetWindow();
        // Assert
        assertEquals(evenLength, output.length);
    }

    public void test_HammingWIndowing_EvenWindowValues() {
        // Arrange
        double[] output = new double[evenLength];
        double[] expected = new double[evenLength];
        // values obtained via  MATLAB long format
        expected[0] = 0.08;
        expected[1] = 0.3978521825;
        expected[2] = 0.912147817;
        expected[3] = 0.912147817;
        expected[4] = 0.3978521825;
        expected[5] = 0.08;

        // Act
        hammingWindowingEven.constructWindow(evenLength);
        output = hammingWindowingEven.GetWindow();
        // Assert
        double sumSquaredDistance = 0;
        for (int i = 0; i < evenLength; i++) {
            sumSquaredDistance += Math.pow(output[i] - expected[i], 2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    public void test_HammingWIndowing_1Input_evenSize_correctOutput() {
        // Arrange
        double[] input = new double[evenLength];
        input[0] = 6;
        input[1] = -3.04;
        input[2] = 0.03;
        input[3] = 8.1;
        input[4] = -2.5;
        input[5] = 9.0002;

        double[] expected = new double[evenLength];
        expected[0] = 0.48;
        expected[1] = -1.2094706;
        expected[2] = 0.0273644345;
        expected[3] = 7.388397321;
        expected[4] = -0.994630456;
        expected[5] = 0.720016;

        // Act
        hammingWindowingEven.constructWindow(evenLength);
        double[] output = hammingWindowingEven.doHammingWindowing(input);
        // Assert
        double sumSquaredDistance = 0;
        for (int i = 0; i < input.length; i++) {
            sumSquaredDistance += Math.pow(output[i] - expected[i], 2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        hammingWindowingOdd = new HammingWindowing();
        hammingWindowingEven = new HammingWindowing();
    }
}
