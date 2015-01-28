package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard deviation of the z accelerometer signal.
 * 
 * @author christiaan, elena
 * 
 */
public class StdLocationZFeatureExtractor extends FeatureExtractor {
    private final StdExtractor stdExtractor;
    private final String name = "std_z";

    @Inject
    public StdLocationZFeatureExtractor(StdExtractor stdExtractor) {
        this.stdExtractor = stdExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix z = dataForInstances.getZ();
        DoubleMatrix zStd = stdExtractor.extractStd(z);
        return zStd;
    }

    @Override
    public String getName() {
        return name;
    }

}
