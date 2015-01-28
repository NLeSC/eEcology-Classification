package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.HammingWindowing;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.WindowedFFTExtractor;

import org.junit.Before;
import org.junit.Test;

public class WindowedFFTExtractorTest {
    private final int oddLength = 5;
    private final int evenLength = 6;
    protected WindowedFFTExtractor winFFTExtractor;

    protected final double errorMargin = 0.0001;

    @Test
    public void windowedFFTExtractorTest_length1Input2lengthOutput() {
        // Arrange
        double[] input = new double[1];
        input[0] = 0;

        // Act
        winFFTExtractor.constructWindowedFFTExtractor(input);
        winFFTExtractor.doWindowedFFTExtraction();
        double[] output = winFFTExtractor.getOutput();
        // Assert
        assertEquals(2, output.length);
    }

    @Test
    public void windowedFFTExtractorTest_length2Input4lengthOutput() {
        // Arrange
        double[] input = new double[2];
        input[0] = 0;
        input[1] = 0;

        // Act
        winFFTExtractor.constructWindowedFFTExtractor(input);
        winFFTExtractor.doWindowedFFTExtraction();
        double[] output = winFFTExtractor.getOutput();
        // Assert
        assertEquals(4, output.length);
    }

    @Test
    public void windowedFFTExtractor_1Input_oddSize_correctOutput() {
        // Arrange
        double[] input = new double[oddLength];
        input[0] = 5;
        input[1] = -3.1;
        input[2] = 2.4;
        input[3] = -8;
        input[4] = 0.5;

        double[] expected = new double[oddLength * 2];
        // Re is at even locations, Im is at odd locations
        expected[0] = -3.154000000000000;
        expected[1] = 0;
        expected[2] = 1.448378860391235;
        expected[3] = -2.319806026475526;
        expected[4] = 1.128621139608765;
        expected[5] = 7.398563711932731;
        expected[6] = 1.128621139608765;
        expected[7] = -7.398563711932731;
        expected[8] = 1.448378860391235;
        expected[9] = 2.319806026475526;

        // Act
        winFFTExtractor.constructWindowedFFTExtractor(input);
        winFFTExtractor.doWindowedFFTExtraction();
        double[] output = winFFTExtractor.getOutput();

        // Assert
        double sumSquaredDistance = 0;
        for (int i = 0; i < input.length; i++) {
            sumSquaredDistance += Math.pow(output[i] - expected[i], 2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    @Test
    public void windowedFFTExtractor_1Input_evenSize_correctOutput() {
        // Arrange
        double[] input = new double[evenLength];
        input[0] = 6;
        input[1] = -3.04;
        input[2] = 0.03;
        input[3] = 8.1;
        input[4] = -2.5;
        input[5] = 9.0002;

        double[] expected = new double[evenLength * 2];
        // Re is at even locations, Im is at odd locations
        expected[0] = 6.411676664028544;
        expected[1] = 0;
        expected[2] = -6.669491627600872;
        expected[3] = 0.785910904093500;
        expected[4] = 8.596757649547309;
        expected[5] = 2.556057980366048;
        expected[6] = -7.386208707921416;
        expected[7] = 0;
        expected[8] = 8.596757649547309;
        expected[9] = -2.556057980366048;
        expected[10] = -6.669491627600872;
        expected[11] = -0.785910904093500;

        // Act
        winFFTExtractor.constructWindowedFFTExtractor(input);
        winFFTExtractor.doWindowedFFTExtraction();
        double[] output = winFFTExtractor.getOutput();

        // Assert
        double sumSquaredDistance = 0;
        for (int i = 0; i < input.length; i++) {
            sumSquaredDistance += Math.pow(output[i] - expected[i], 2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        winFFTExtractor = new WindowedFFTExtractor();
        winFFTExtractor.setHammingWindowing(new HammingWindowing());
        winFFTExtractor.setMinBins(0);
    }
}
