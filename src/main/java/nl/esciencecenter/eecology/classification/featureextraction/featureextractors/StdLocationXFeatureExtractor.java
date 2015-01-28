package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard deviation of the x accelerometer signal.
 * 
 * @author christiaan, elena
 * 
 */
public class StdLocationXFeatureExtractor extends FeatureExtractor {
    private final StdExtractor stdExtractor;
    private final String name = "std_x";

    @Inject
    public StdLocationXFeatureExtractor(StdExtractor stdExtractor) {
        this.stdExtractor = stdExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix x = dataForInstances.getX();
        DoubleMatrix xStd = stdExtractor.extractStd(x);
        return xStd;
    }

    @Override
    public String getName() {
        return name;
    }

}
