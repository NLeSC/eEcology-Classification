package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard mean along dimension x of the accelerometer signals
 * 
 * @author christiaan, Elena
 * 
 */
public class MeanLocationXFeatureExtractor extends FeatureExtractor {
    private final MeanExtractor meanExtractor;
    private final String name = "mean_x";

    @Inject
    public MeanLocationXFeatureExtractor(MeanExtractor meanExtractor) {
        this.meanExtractor = meanExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix x = dataForInstances.getX();
        DoubleMatrix xMean = meanExtractor.extractMean(x);
        return xMean;

    }

    @Override
    public String getName() {
        return name;
    }
}
