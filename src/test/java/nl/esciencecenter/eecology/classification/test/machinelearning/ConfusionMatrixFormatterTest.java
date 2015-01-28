package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import nl.esciencecenter.eecology.classification.commands.ConfusionMatrixFormatter;
import nl.esciencecenter.eecology.classification.commands.ConfusionMatrixStatisticsCalculator;

import org.junit.Before;
import org.junit.Test;

public class ConfusionMatrixFormatterTest {

    private ConfusionMatrixFormatter confusionMatrixFormatter;

    @Test
    public void format_someTestData_noExceptionThrown() {
        // Arrange
        List<String> labels = getTestLabels();
        double[][] matrix = getTestMatrix();

        // Act
        String output = confusionMatrixFormatter.format(matrix, labels);

        // Assert
    }

    @Test
    public void format_someTestData_noCorrectOutput() {
        // Arrange
        List<String> labels = getTestLabels();
        double[][] matrix = getTestMatrix();
        String expected = getExpectedOutput();

        // Act
        String output = confusionMatrixFormatter.format(matrix, labels);

        // Assert
        assertEquals(expected, output);
    }

    @Before
    public void setUp() {
        confusionMatrixFormatter = new ConfusionMatrixFormatter();
        confusionMatrixFormatter.setCellWidth(6);
        confusionMatrixFormatter.setConfusionMatrixStatisticsCalculator(new ConfusionMatrixStatisticsCalculator());
    }

    private String getExpectedOutput() {
        //@formatter:off
        String expected =   "Confusion matrix\n" +
                "           Predicted classes:\n" +
                "Actual:    aaaa  bb    chris \n" +
                "aaaa       5     0     1     83%\n" +
                "bb         2     6     0     75%\n" +
                "christiaan 1     1     12    86%\n" +
                "           ------------------\n" +
                "Correct:   63%   86%   92%   \n";
        //@formatter:off
        return expected;
    }

    private double[][] getTestMatrix() {
        return new double[][] { { 5, 0, 1 }, { 2, 6, 0 }, { 1, 1, 12 } };
    }

    private List<String> getTestLabels() {
        return Arrays.asList(new String[] { "aaaa", "bb", "christiaan" });
    }

}
