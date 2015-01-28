package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

public class NoisePerAbsDerYFeatureExtractor extends NoisePerAbsDerFeatureExtractor {
    private final NoiseYFeatureExtractor noiseYFeatureExtractor;
    private final MeanAbsDerYFeatureExtractor meanAbsDerYFeatureExtractor;

    @Inject
    public NoisePerAbsDerYFeatureExtractor(NoiseYFeatureExtractor noiseYFeatureExtractor,
            MeanAbsDerYFeatureExtractor meanAbsDerYFeatureExtractor) {
        this.noiseYFeatureExtractor = noiseYFeatureExtractor;
        this.meanAbsDerYFeatureExtractor = meanAbsDerYFeatureExtractor;
    }

    /**
     * Calculates the noise divided by the mean of the absolute derivative of the sequence. If the mean of the absolute derivative
     * is zero, so is the result. This ensures that the result is never NaN.
     */
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix m = formattedSegments.getY();
        DoubleMatrix noise = noiseYFeatureExtractor.extractFeatures(formattedSegments);
        DoubleMatrix derivative = meanAbsDerYFeatureExtractor.extractFeatures(formattedSegments);

        return extractFeatures(m, noise, derivative);
    }

    @Override
    public String getName() {
        return "noise/absder_y";
    }

}
