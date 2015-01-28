package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqMagnitudeXFeatureExtractor;

import org.junit.Before;

public class FundFreqMagnitudeXFeatureExtractorTest extends FundFreqMagnitudeFeatureExtractorTest {

    @Before
    public void setUp() {
        FundFreqMagnitudeXFeatureExtractor fundFreqCorrelationXFeatureExtractor = new FundFreqMagnitudeXFeatureExtractor();
        fundFreqCorrelationXFeatureExtractor.setFundFreqMagnitudeExtractor(getFundFreqMagnitudeExtractor());
        fundFreqCorrelationXFeatureExtractor.setAccelerometerSampleFrequency(20);
        featureExtractor = fundFreqCorrelationXFeatureExtractor;
    }
}
