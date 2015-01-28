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
public class FundFreqMagnitudeZFeatureExtractor extends FeatureExtractor {
    @Inject
    private FundFreqMagnitudeExtractor fundFreqMagnitudeExtractor;

    @Inject
    @Named("accelerometer_sample_frequency")
    private double accelerometerSampleFrequency;

    public void setAccelerometerSampleFrequency(double accelerometerSampleFrequency) {
        this.accelerometerSampleFrequency = accelerometerSampleFrequency;
    }

    public void setFundFreqMagnitudeExtractor(FundFreqMagnitudeExtractor fundFreqMagnitudeExtractor) {
        this.fundFreqMagnitudeExtractor = fundFreqMagnitudeExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix input = dataForInstances.getZ();
        return fundFreqMagnitudeExtractor.extractFundFreqMagnitude(input, accelerometerSampleFrequency);
    }

    @Override
    public String getName() {
        return "fundfreqmagnitude_z";
    }
}
