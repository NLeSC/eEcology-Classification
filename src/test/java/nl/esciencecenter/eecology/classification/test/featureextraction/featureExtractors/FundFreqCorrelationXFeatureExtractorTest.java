package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqCorrelationXFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class FundFreqCorrelationXFeatureExtractorTest extends FundFreqCorrelationFeatureExtractorTest {
    @Test
    public void extractFeatures_onlyDataInX_nonZeroResult() {
        // Arrange
        DoubleMatrix x = new DoubleMatrix(random);
        DoubleMatrix y = new DoubleMatrix(x.rows, x.columns);
        DoubleMatrix z = new DoubleMatrix(x.rows, x.columns);
        DoubleMatrix gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments formattedSegments = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix result = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertNotEquals(0, result.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_zerosInX_zeroResult() {
        // Arrange
        DoubleMatrix z = new DoubleMatrix(random);
        DoubleMatrix y = new DoubleMatrix(random);
        DoubleMatrix x = new DoubleMatrix(z.rows, z.columns);
        DoubleMatrix gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments formattedSegments = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix result = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals(0, result.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() {
        FundFreqCorrelationXFeatureExtractor fundFreqCorrelationXFeatureExtractor = new FundFreqCorrelationXFeatureExtractor();
        fundFreqCorrelationXFeatureExtractor.setFundFreqCorrelationExtractor(getFundFreqCorrelationExtractor());
        fundFreqCorrelationXFeatureExtractor.setAccelerometerSampleFrequency(20);
        featureExtractor = fundFreqCorrelationXFeatureExtractor;
    }
}
