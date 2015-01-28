package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

public class NoisePerAbsDerXFeatureExtractor extends NoisePerAbsDerFeatureExtractor {
    private final NoiseXFeatureExtractor noiseXFeatureExtractor;
    private final MeanAbsDerXFeatureExtractor meanAbsDerXFeatureExtractor;

    @Inject
    public NoisePerAbsDerXFeatureExtractor(NoiseXFeatureExtractor noiseXFeatureExtractor,
            MeanAbsDerXFeatureExtractor meanAbsDerXFeatureExtractor) {
        this.noiseXFeatureExtractor = noiseXFeatureExtractor;
        this.meanAbsDerXFeatureExtractor = meanAbsDerXFeatureExtractor;
    }

    /**
     * Calculates the noise divided by the mean of the absolute derivative of the sequence. If the mean of the absolute derivative
     * is zero, so is the result. This ensures that the result is never NaN.
     */
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix m = formattedSegments.getX();
        DoubleMatrix noise = noiseXFeatureExtractor.extractFeatures(formattedSegments);
        DoubleMatrix derivative = meanAbsDerXFeatureExtractor.extractFeatures(formattedSegments);

        return extractFeatures(m, noise, derivative);
    }

    @Override
    public String getName() {
        return "noise/absder_x";
    }

}
