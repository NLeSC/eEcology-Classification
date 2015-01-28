package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard deviation of the y accelerometer signal.
 * 
 * @author christiaan, elena
 * 
 */
public class StdLocationYFeatureExtractor extends FeatureExtractor {
    private final StdExtractor stdExtractor;
    private final String name = "std_y";

    @Inject
    public StdLocationYFeatureExtractor(StdExtractor stdExtractor) {
        this.stdExtractor = stdExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix y = dataForInstances.getY();
        DoubleMatrix yStd = stdExtractor.extractStd(y);
        return yStd;
    }

    @Override
    public String getName() {
        return name;
    }

}
