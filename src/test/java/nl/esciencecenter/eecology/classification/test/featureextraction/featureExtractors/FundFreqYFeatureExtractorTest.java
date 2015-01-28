package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqYFeatureExtractor;

import org.junit.Before;

/**
 * @author Elena and Christiaan
 *
 */
public class FundFreqYFeatureExtractorTest extends FundFreqFeatureExtractorTest {

    @Before
    public void setUp() {
        FundFreqYFeatureExtractor fundFreqYFeatureExtractor = new FundFreqYFeatureExtractor();
        fundFreqYFeatureExtractor.setFundFreqExtractor(getFundFreqExtractor());
        featureExtractor = fundFreqYFeatureExtractor;
    }
}
