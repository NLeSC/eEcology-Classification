package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the mean pitch feature.
 * 
 * @author Elena Ranguelova
 */

public class MeanRollFeatureExtractor extends FeatureExtractor {

    private final RollFeatureExtractor rollExtractor;
    private final MeanExtractor meanExtractor;
    private final String name = "mean_roll";

    /**
     * Calculates the mean roll feature using DoubleMatrix type. roll = (atan(y./sqrt(x.^2+z.^2))*180)/pi. The mean is taken
     * row-wise.
     * 
     * @param rollExtractor
     * 
     */
    @Inject
    public MeanRollFeatureExtractor(RollFeatureExtractor rollExtractor, MeanExtractor meanExtractor) {
        this.rollExtractor = rollExtractor;
        this.meanExtractor = meanExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {

        DoubleMatrix rollDegrees = rollExtractor.extractFeatures(dataForInstances);

        // take the mean per rows
        DoubleMatrix meanRoll = meanExtractor.extractMean(rollDegrees);
        return meanRoll;
    }

    @Override
    public String getName() {
        return name;
    }
}
