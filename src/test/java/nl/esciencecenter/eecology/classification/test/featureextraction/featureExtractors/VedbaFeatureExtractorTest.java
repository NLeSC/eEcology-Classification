package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.Convolver;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.VedbaFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class VedbaFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void extractFeatures_inputFromRealDataset_correctResult() {
        // Arrange
        DoubleMatrix x = new DoubleMatrix(
                new double[][] { { -0.0871, -0.0129, 0.3767, 0.4782, -0.1167, -0.0035, -0.1362, -0.0769, 0.3525, 0.5969, -0.0753,
                    0.0105, -0.1847, -0.0535, 0.3814, 0.4290, -0.1394, -0.0129, -0.1191, -0.0066 } });
        DoubleMatrix y = new DoubleMatrix(
                new double[][] { { -0.0701, -0.0416, -0.0259, -0.1517, 0.0049, -0.0491, -0.1285, -0.0978, 0.1442, -0.1008,
                    -0.0746, 0.0109, -0.1450, 0.0641, 0.0798, -0.1428, 0.0259, -0.1120, -0.1690, -0.0491 } });
        DoubleMatrix z = new DoubleMatrix(new double[][] { { 0.8844, -0.0045, 0.6624, 1.2548, 1.6005, 1.8964, 0.6378, -0.0537,
            0.6378, 1.3285, 1.6005, 1.8472, 0.5879, 0.0700, 0.7116, 1.2794, 1.5752, 1.8226, 0.6624, -0.0537 } });
        DoubleMatrix gpsSpeed = new DoubleMatrix(1, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        // expectation according to matlab by Willem's script from mail za 15-3-2014 22:51,
        // but divided by nMeasurements (https://services.e-ecology.sara.nl/redmine/issues/411)
        double expected = 0.16901;

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(expected, output.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new VedbaFeatureExtractor(new Convolver());
    }
}
