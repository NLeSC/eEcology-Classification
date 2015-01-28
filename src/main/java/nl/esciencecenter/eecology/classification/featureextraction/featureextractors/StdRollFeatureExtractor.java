package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard deviation of the roll feature.
 *
 * @author Elena Ranguelova
 */

public class StdRollFeatureExtractor extends FeatureExtractor {

    private RollFeatureExtractor rollExtractor;
    private final StdExtractor stdExtractor;
    private final String name = "std_roll";

    public void setRollExtractor(RollFeatureExtractor rollExtractor) {
        this.rollExtractor = rollExtractor;
    }

    /**
     * Calculates the std of the roll feature using DoubleMatrix type. roll = (atan(y./sqrt(x.^2+z.^2))*180)/pi.
     *
     * @param pitchExtractor
     *            ;
     *
     */
    @Inject
    public StdRollFeatureExtractor(RollFeatureExtractor rollExtractor, StdExtractor stdExtractor) {
        this.rollExtractor = rollExtractor;
        this.stdExtractor = stdExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {

        DoubleMatrix rollDegrees = rollExtractor.extractFeatures(dataForInstances);

        // take the mean per rows
        DoubleMatrix stdRoll = stdExtractor.extractStd(rollDegrees);
        return stdRoll;
    }

    @Override
    public String getName() {
        return name;
    }

}
