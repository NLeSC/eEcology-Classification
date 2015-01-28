package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard mean of the y accelerometer signal
 * 
 * @author Elena
 * 
 */
public class MeanLocationYFeatureExtractor extends FeatureExtractor {
    private final MeanExtractor meanExtractor;
    private final String name = "mean_y";

    @Inject
    public MeanLocationYFeatureExtractor(MeanExtractor meanExtractor) {
        this.meanExtractor = meanExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix y = dataForInstances.getY();
        DoubleMatrix yMean = meanExtractor.extractMean(y);
        return yMean;

    }

    @Override
    public String getName() {
        return name;
    }
}
