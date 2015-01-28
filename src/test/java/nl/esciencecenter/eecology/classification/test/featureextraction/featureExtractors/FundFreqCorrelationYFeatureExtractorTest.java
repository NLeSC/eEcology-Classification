package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqCorrelationYFeatureExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class FundFreqCorrelationYFeatureExtractorTest extends FundFreqCorrelationFeatureExtractorTest {
    @Test
    public void extractFeatures_onlyDataInY_nonZeroResult() {
        // Arrange
        DoubleMatrix y = new DoubleMatrix(random);
        DoubleMatrix x = new DoubleMatrix(y.rows, y.columns);
        DoubleMatrix z = new DoubleMatrix(y.rows, y.columns);
        DoubleMatrix gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments formattedSegments = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix result = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertNotEquals(0, result.get(0, 0), errorMargin);
    }

    @Test
    public void extractFeatures_zerosInY_zeroResult() {
        // Arrange
        DoubleMatrix x = new DoubleMatrix(random);
        DoubleMatrix z = new DoubleMatrix(random);
        DoubleMatrix y = new DoubleMatrix(z.rows, z.columns);
        DoubleMatrix gpsSpeed = new DoubleMatrix(x.rows, 1);
        FormattedSegments formattedSegments = new FormattedSegments(x, y, z, gpsSpeed);

        // Act
        DoubleMatrix result = featureExtractor.extractFeatures(formattedSegments);

        // Assert
        assertEquals(0, result.get(0, 0), errorMargin);
    }

    @Before
    public void setUp() {
        FundFreqCorrelationYFeatureExtractor fundFreqCorrelationYFeatureExtractor = new FundFreqCorrelationYFeatureExtractor();
        fundFreqCorrelationYFeatureExtractor.setFundFreqCorrelationExtractor(getFundFreqCorrelationExtractor());
        fundFreqCorrelationYFeatureExtractor.setAccelerometerSampleFrequency(20);
        featureExtractor = fundFreqCorrelationYFeatureExtractor;
    }
}
