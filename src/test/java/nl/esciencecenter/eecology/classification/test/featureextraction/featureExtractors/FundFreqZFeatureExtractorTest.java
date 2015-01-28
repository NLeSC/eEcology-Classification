package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqZFeatureExtractor;

import org.junit.Before;

/**
 * @author Elena
 *
 */
public class FundFreqZFeatureExtractorTest extends FundFreqFeatureExtractorTest {

    @Before
    public void setUp() {
        FundFreqZFeatureExtractor fundFreqZFeatureExtractor = new FundFreqZFeatureExtractor();
        fundFreqZFeatureExtractor.setFundFreqExtractor(getFundFreqExtractor());
        featureExtractor = fundFreqZFeatureExtractor;
    }
}
