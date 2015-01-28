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

public class MeanPitchFeatureExtractor extends FeatureExtractor {

    private final PitchFeatureExtractor pitchExtractor;
    private final MeanExtractor meanExtractor;
    private final String name = "mean_pitch";

    /**
     * Calculates the mean pitch feature using DoubleMatrix type. pitch = (atan(x./sqrt(y.^2+z.^2))*180)/pi. The mean is taken
     * row-wise.
     * 
     * @param pitchExtractor
     *            ;
     * 
     */
    @Inject
    public MeanPitchFeatureExtractor(PitchFeatureExtractor pitchExtractor, MeanExtractor meanExtractor) {
        this.pitchExtractor = pitchExtractor;
        this.meanExtractor = meanExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {

        DoubleMatrix pitchDegrees = pitchExtractor.extractFeatures(dataForInstances);

        // take the mean per rows
        DoubleMatrix meanPitch = meanExtractor.extractMean(pitchDegrees);
        return meanPitch;
    }

    @Override
    public String getName() {
        return name;
    }

}
