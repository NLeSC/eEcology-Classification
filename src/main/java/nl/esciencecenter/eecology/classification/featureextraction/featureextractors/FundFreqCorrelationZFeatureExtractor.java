package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Calculates the maximum correlation with fundamental frequency along dimension x of the accelerometer signals.
 *
 * @author Christiaan Meijer
 *
 */
public class FundFreqCorrelationZFeatureExtractor extends FeatureExtractor {
    @Inject
    private FundFreqCorrelationExtractor fundFreqCorrelationExtractor;

    @Inject
    @Named("accelerometer_sample_frequency")
    private double accelerometerSampleFrequency;

    public void setAccelerometerSampleFrequency(double accelerometerSampleFrequency) {
        this.accelerometerSampleFrequency = accelerometerSampleFrequency;
    }

    public void setFundFreqCorrelationExtractor(FundFreqCorrelationExtractor fundFreqCorrelationExtractor) {
        this.fundFreqCorrelationExtractor = fundFreqCorrelationExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix input = dataForInstances.getZ();
        return fundFreqCorrelationExtractor.extractFundFreqCorrelation(input, accelerometerSampleFrequency);
    }

    @Override
    public String getName() {
        return "fundfreqcorr_z";
    }
}
