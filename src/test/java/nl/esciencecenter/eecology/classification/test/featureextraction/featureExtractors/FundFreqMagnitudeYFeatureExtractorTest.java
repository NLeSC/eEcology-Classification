package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqMagnitudeYFeatureExtractor;

import org.junit.Before;

public class FundFreqMagnitudeYFeatureExtractorTest extends FundFreqMagnitudeFeatureExtractorTest {

    @Before
    public void setUp() {
        FundFreqMagnitudeYFeatureExtractor fundFreqMagnitudeYFeatureExtractor = new FundFreqMagnitudeYFeatureExtractor();
        fundFreqMagnitudeYFeatureExtractor.setFundFreqMagnitudeExtractor(getFundFreqMagnitudeExtractor());
        fundFreqMagnitudeYFeatureExtractor.setAccelerometerSampleFrequency(20);
        featureExtractor = fundFreqMagnitudeYFeatureExtractor;
    }
}
