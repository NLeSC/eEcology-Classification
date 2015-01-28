package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqMagnitudeZFeatureExtractor;

import org.junit.Before;

public class FundFreqMagnitudeZFeatureExtractorTest extends FundFreqMagnitudeFeatureExtractorTest {

    @Before
    public void setUp() {
        FundFreqMagnitudeZFeatureExtractor fundFreqMagnitudeZFeatureExtractor = new FundFreqMagnitudeZFeatureExtractor();
        fundFreqMagnitudeZFeatureExtractor.setFundFreqMagnitudeExtractor(getFundFreqMagnitudeExtractor());
        fundFreqMagnitudeZFeatureExtractor.setAccelerometerSampleFrequency(20);
        featureExtractor = fundFreqMagnitudeZFeatureExtractor;
    }
}
