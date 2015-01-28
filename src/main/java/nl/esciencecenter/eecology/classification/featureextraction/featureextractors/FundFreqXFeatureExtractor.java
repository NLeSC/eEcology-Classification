package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the fundamental frequency along dimension x of the accelerometer signals
 *
 * @author Elena Ranguelova
 *
 */
public class FundFreqXFeatureExtractor extends FeatureExtractor {
    @Inject
    private FundFreqExtractor fundFreqExtractor;

    public void setFundFreqExtractor(FundFreqExtractor fundFreqExtractor) {
        this.fundFreqExtractor = fundFreqExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix input = dataForInstances.getX();
        return fundFreqExtractor.extractFrequency(input);
    }

    @Override
    public String getName() {
        return "fundfreq_x";
    }
}
