package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.Convolver;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.OdbaFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class OdbaFeatureExtractorTest extends FeatureExtractorTest {
    @Test
    public void extractFeatures_arbitraryInput_correctResult() {
        // Arrange
        DoubleMatrix x = new DoubleMatrix(new double[][] { { -0.7867, 0.9238, -0.9907, 0.5498, 0.6346, 0.7374, -0.8311, -0.2004,
                -0.4803, 0.6001 } });
        DoubleMatrix y = x.mul(2);
        DoubleMatrix z = x.mul(4);
        DoubleMatrix gpsSpeed = new DoubleMatrix(1, 1);
        FormattedSegments input = createFormattedSegments(x, y, z, gpsSpeed);
        // expectation according to matlab by Willem's script from mail za 15-3-2014 22:51,
        // but divided by nMeasurements (https://services.e-ecology.sara.nl/redmine/issues/411)
        double expected = 4.125114;

        // Act
        DoubleMatrix output = featureExtractor.extractFeatures(input);

        // Assert
        assertEquals(expected, output.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() throws Exception {
        featureExtractor = new OdbaFeatureExtractor(new Convolver());
    }
}
