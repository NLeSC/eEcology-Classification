package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CorrelationXYFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class CorrelationXYFeatureExtractorTest extends FeatureExtractorTest {
    private final DoubleMatrix realisticTestx = new DoubleMatrix(new double[][] { { -0.238983571318901, -0.227468140641793,
        -0.238983571318901, -0.235145094426532, -0.235145094426532, -0.234377399048058, -0.230538922155689,
        -0.235145094426532, -0.233609703669584, -0.230538922155689, -0.227468140641793, -0.227468140641793,
        -0.229771226777215, -0.231306617534162, -0.227468140641793, -0.229003531398741, -0.238983571318901,
        -0.221326577614003, -0.224397359127898, -0.228235836020267 } });
    private final DoubleMatrix realisticTesty = new DoubleMatrix(new double[][] { { -0.143940531611353, -0.140186214146268,
        -0.143189668118336, -0.141687941132302, -0.143940531611353, -0.146193122090404, -0.143189668118336,
        -0.143940531611353, -0.145442258597387, -0.14469139510437, -0.145442258597387, -0.14469139510437, -0.147694849076438,
        -0.142438804625319, -0.140186214146268, -0.143189668118336, -0.14469139510437, -0.14469139510437, -0.142438804625319,
        -0.141687941132302 } });
    private final DoubleMatrix realisticTestz = new DoubleMatrix(new double[][] { { 0.977863182167563, 0.977863182167563,
        0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563,
        0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563,
        0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563, 0.977863182167563 } });
    private final DoubleMatrix test1x = new DoubleMatrix(new double[][] { { 0, 1, 2 }, { 3, 4, 5 } });
    private final DoubleMatrix test1y = new DoubleMatrix(new double[][] { { 1, 0, 0 }, { 0, 0, 1 } });
    private final DoubleMatrix test1z = new DoubleMatrix(new double[][] { { 2, 1, 0 }, { 5, 4, 3 } });
    private final DoubleMatrix test1xyExpected = new DoubleMatrix(new double[][] { { -0.86601 }, { 0.86604 } }); // according to matlab
    private final DoubleMatrix test1yzExpected = new DoubleMatrix(new double[][] { { 0.8660 }, { -0.8660 } }); // according to matlab
    private final DoubleMatrix test1xzExpected = new DoubleMatrix(new double[][] { { -1 }, { -1 } }); // according to matlab

    @Test
    public void extractFeatures_2SequenceVectors_correctOutput() {
        DoubleMatrix gpsSpeed = new DoubleMatrix(test1x.rows, 1);
        FormattedSegments input = createFormattedSegments(test1x, test1y, test1z, gpsSpeed);

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(test1xyExpected.get(0, 0), output.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_someInput_noNaNs() {
        DoubleMatrix gpsSpeed = new DoubleMatrix(realisticTestx.rows, 1);
        FormattedSegments input = createFormattedSegments(realisticTestx, realisticTesty, realisticTestz, gpsSpeed);

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertTrue(Double.isNaN(output.get(0, 0)) == false);
    }

    @Test
    public void extractFeatures_constantyAndz_onlyZeros() {
        // Arrange
        DoubleMatrix x, y, z, gpsSpeed;
        x = new DoubleMatrix(new double[][] { { 1, 2, 3 } });
        y = new DoubleMatrix(new double[][] { { 4, 4, 4 } });
        z = new DoubleMatrix(new double[][] { { 0, 0, 0 } });
        gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        double expected = 0;

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(expected, output.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new CorrelationXYFeatureExtractor();
    }
}
