package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;
import junit.framework.TestCase;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.ArrayMeanRemover;

public class ArrayMeanRemovalTest extends TestCase{
    protected ArrayMeanRemover arrayMeanRemoval;

    protected final double errorMargin = 0.0001;

    public void test_arrayMeanRemovalTest_length1Input1lengthOutput() {
        // Arrange
        double[] input = new double[1];
        input[0] = 0;

        // Act
        arrayMeanRemoval.constructArrayMeanRemoval(input);
        arrayMeanRemoval.removeMean();
        double[] output = arrayMeanRemoval.getOutput();
        // Assert
        assertEquals(1, output.length);
    }

    public void test_arrayMeanRemovalTest_ZeroInputZeroOutput() {
        // Arrange
        double[] input = new double[3];
        input[0] = 0;
        input[1] = 0;
        input[2] = 0;

        double[] expected = new double[3];
        expected[0] = 0;
        expected[1] = 0;
        expected[2] = 0;

        // Act
        arrayMeanRemoval.constructArrayMeanRemoval(input);
        arrayMeanRemoval.removeMean();
        double[] output = arrayMeanRemoval.getOutput();

        // Assert
        double sumSquaredDistance = 0;
        for (int i=0;i<input.length;i++) {
            sumSquaredDistance += Math.pow(output[i]-expected[i],2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    public void test_arrayMeanRemovalTest_CorrectMean() {
        // Arrange
        double[] input = new double[3];
        input[0] = 0.8;
        input[1] = -0.6;
        input[2] = 9.41;

        double expected = 3.2033;
        // Act
        arrayMeanRemoval.constructArrayMeanRemoval(input);
        arrayMeanRemoval.calculateMean();
        double mean = arrayMeanRemoval.GetMean();

        // Assert
        double error = Math.abs(mean - expected);
        assertTrue(error < errorMargin);
    }


    public void test_arrayMeanRemovalTest_CorrectMeanRemoval() {
        // Arrange
        double[] input = new double[5];
        input[0] = 0;
        input[1] = 0.809016994374947;
        input[2] =  0.951056516295154;
        input[3] =  0.309016994374947;
        input[4] = -0.587785252292473;

        double[] expected = new double[5];
        expected[0] =  -0.296261050550515;
        expected[1] =  0.512755943824432;
        expected[2] =  0.654795465744639;
        expected[3] =  0.012755943824432;
        expected[4] =  -0.884046302842988;

        // Act
        arrayMeanRemoval.constructArrayMeanRemoval(input);
        arrayMeanRemoval.removeMean();
        double[] output = arrayMeanRemoval.getOutput();

        // Assert
        double sumSquaredDistance = 0;
        for (int i=0;i<5;i++) {
            sumSquaredDistance += Math.pow(output[i]-expected[i],2);
        }
        double error = Math.sqrt(sumSquaredDistance);
        assertTrue(error < errorMargin);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        arrayMeanRemoval = new ArrayMeanRemover();
    }
}
