package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqCorrelationZFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class FundFreqCorrelationZFeatureExtractorTest extends FundFreqCorrelationFeatureExtractorTest {
    @Test
    public void extractFeatures_onlyDataInZ_nonZeroResult() {
        // Arrange
        DoubleMatrix z = new DoubleMatrix(random);
        DoubleMatrix x = new DoubleMatrix(z.rows, z.columns);
        DoubleMatrix y = new DoubleMatrix(z.rows, z.columns);
        DoubleMatrix gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments formattedSegments = createFormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix result = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertNotEquals(0, result.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_zerosInZ_zeroResult() {
        // Arrange
        DoubleMatrix x = new DoubleMatrix(random);
        DoubleMatrix y = new DoubleMatrix(random);
        DoubleMatrix z = new DoubleMatrix(x.rows, x.columns);
        DoubleMatrix gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments formattedSegments = createFormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix result = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals(0, result.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() {
        FundFreqCorrelationZFeatureExtractor fundFreqCorrelationZFeatureExtractor = new FundFreqCorrelationZFeatureExtractor();
        fundFreqCorrelationZFeatureExtractor.setFundFreqCorrelationExtractor(getFundFreqCorrelationExtractor());
        fundFreqCorrelationZFeatureExtractor.setAccelerometerSampleFrequency(20);
        featureExtractor = fundFreqCorrelationZFeatureExtractor;
    }
}
