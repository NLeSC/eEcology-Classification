package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard mean of the z accelerometer signal
 * 
 * @author Elena
 * 
 */
public class MeanLocationZFeatureExtractor extends FeatureExtractor {
    private final MeanExtractor meanExtractor;
    private final String name = "mean_z";

    @Inject
    public MeanLocationZFeatureExtractor(MeanExtractor meanExtractor) {
        this.meanExtractor = meanExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix z = dataForInstances.getZ();
        DoubleMatrix zMean = meanExtractor.extractMean(z);
        return zMean;

    }

    @Override
    public String getName() {
        return name;
    }
}
