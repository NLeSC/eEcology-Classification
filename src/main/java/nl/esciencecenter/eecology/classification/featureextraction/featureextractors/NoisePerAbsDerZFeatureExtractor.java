package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

public class NoisePerAbsDerZFeatureExtractor extends NoisePerAbsDerFeatureExtractor {
    private final NoiseZFeatureExtractor noiseZFeatureExtractor;
    private final MeanAbsDerZFeatureExtractor meanAbsDerZFeatureExtractor;

    @Inject
    public NoisePerAbsDerZFeatureExtractor(NoiseZFeatureExtractor noiseZFeatureExtractor,
            MeanAbsDerZFeatureExtractor meanAbsderZFeatureExtractor) {
        this.noiseZFeatureExtractor = noiseZFeatureExtractor;
        meanAbsDerZFeatureExtractor = meanAbsderZFeatureExtractor;
    }

    /**
     * Calculates the noise divided by the mean of the absolute derivative of the sequence. If the mean of the absolute derivative
     * is zero, so is the result. This ensures that the result is never NaN.
     */
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix m = formattedSegments.getX();
        DoubleMatrix noise = noiseZFeatureExtractor.extractFeatures(formattedSegments);
        DoubleMatrix derivative = meanAbsDerZFeatureExtractor.extractFeatures(formattedSegments);

        return extractFeatures(m, noise, derivative);
    }

    @Override
    public String getName() {
        return "noise/absder_z";
    }

}
