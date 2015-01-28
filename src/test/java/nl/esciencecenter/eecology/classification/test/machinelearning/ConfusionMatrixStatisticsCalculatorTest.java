package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.junit.Assert.assertArrayEquals;
import nl.esciencecenter.eecology.classification.commands.ConfusionMatrixStatisticsCalculator;

import org.junit.Before;
import org.junit.Test;

public class ConfusionMatrixStatisticsCalculatorTest {

    private ConfusionMatrixStatisticsCalculator confusionMatrixStatisticsCalculator;

    private final double[] precisionVector = { 0.8526315789473684, 0.9835164835164835, 0.861878453038674, 0.65, 0.8, Double.NaN,
            0.8484848484848485, 0.5238095238095238, Double.NaN };
    private final double[] recallVector = { 0.8901098901098901, 0.9675675675675676, 0.8342245989304813, 0.9285714285714286,
            0.8666666666666667, 0.0, 0.7304347826086957, 0.88, Double.NaN };
    private final double[][] confusionMatrix = { { 81.0, 0.0, 1.0, 0.0, 8.0, 0.0, 1.0, 0.0, 0.0 },
            { 0.0, 179.0, 1.0, 0.0, 0.0, 0.0, 0.0, 5.0, 0.0 }, { 4.0, 1.0, 156.0, 3.0, 2.0, 0.0, 12.0, 9.0, 0.0 },
            { 0.0, 0.0, 0.0, 13.0, 0.0, 0.0, 1.0, 0.0, 0.0 }, { 7.0, 0.0, 0.0, 1.0, 52.0, 0.0, 0.0, 0.0, 0.0 },
            { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 6.0, 0.0 }, { 3.0, 0.0, 22.0, 3.0, 3.0, 0.0, 84.0, 0.0, 0.0 },
            { 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 22.0, 0.0 }, { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 } };

    private final double errorMargin = 0.0001;

    @Test
    public void getPrecisionVector_testConfusiontMatrix_correctResult() {
        // Arrange

        // Act
        double[] output = confusionMatrixStatisticsCalculator.getPrecisionVector(confusionMatrix);

        // Assert
        assertArrayEquals(precisionVector, output, errorMargin);
    }

    @Test
    public void getRecallVector_testConfusiontMatrix_correctResult() {
        // Arrange

        // Act
        double[] output = confusionMatrixStatisticsCalculator.getRecallVector(confusionMatrix);

        // Assert
        assertArrayEquals(recallVector, output, errorMargin);
    }

    @Before
    public void setUp() {
        confusionMatrixStatisticsCalculator = new ConfusionMatrixStatisticsCalculator();
    }

}
