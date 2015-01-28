package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqMagnitudeExtractor;

import org.jblas.DoubleMatrix;

public abstract class FundFreqMagnitudeFeatureExtractorTest extends FeatureExtractorTest {

    protected final double[][] random = { { 0.1017, 0.9954, 0.3321, 0.2973, 0.0620, 0.2982, 0.0464, 0.5054, 0.7614, 0.6311,
            0.0899, 0.0809, 0.7772, 0.9051, 0.5338, 0.1092, 0.8258, 0.3381, 0.2940, 0.7463 } };

    protected FundFreqMagnitudeExtractor getFundFreqMagnitudeExtractor() {
        FundFreqMagnitudeExtractor fundFreqMagnitudeExtractor = new FundFreqMagnitudeExtractor();
        fundFreqMagnitudeExtractor.setFundFreqFeatureExtractor(getDummyFundFreqExtractorReturningTwo());
        return fundFreqMagnitudeExtractor;
    }

    private FundFreqExtractor getDummyFundFreqExtractorReturningTwo() {
        return new FundFreqExtractor() {
            @Override
            protected DoubleMatrix extractMagnitudes(DoubleMatrix input) {
                DoubleMatrix zeros = new DoubleMatrix(input.rows, 1);
                DoubleMatrix alwaysTwo = zeros.add(2);
                return alwaysTwo;
            }
        };
    }

}