package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

public class NoiseZFeatureExtractor extends NoiseFeatureExtractor {
    /**
     * Calculates the mean of the absolute derivative of the sequence.
     */
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        return calculateNoise(formattedSegments.getZ());
    }

    @Override
    public String getName() {
        return "noise_z";
    }

}
