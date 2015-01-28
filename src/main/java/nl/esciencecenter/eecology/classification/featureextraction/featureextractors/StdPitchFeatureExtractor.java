package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard deviation of the pitch feature.
 *
 * @author Elena Ranguelova
 */

public class StdPitchFeatureExtractor extends FeatureExtractor {

    private PitchFeatureExtractor pitchExtractor;
    private final StdExtractor stdExtractor;
    private final String name = "std_pitch";

    public void setPitchExtractor(PitchFeatureExtractor pitchExtractor) {
        this.pitchExtractor = pitchExtractor;
    }

    /**
     * Calculates the std of the pitch feature using DoubleMatrix type. pitch = (atan(x./sqrt(y.^2+z.^2))*180)/pi.
     *
     * @param pitchExtractor
     *
     */
    @Inject
    public StdPitchFeatureExtractor(PitchFeatureExtractor pitchExtractor, StdExtractor stdExtractor) {
        this.pitchExtractor = pitchExtractor;
        this.stdExtractor = stdExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {

        DoubleMatrix pitchDegrees = pitchExtractor.extractFeatures(dataForInstances);

        // take the mean per rows
        DoubleMatrix stdPitch = stdExtractor.extractStd(pitchDegrees);
        return stdPitch;
    }

    @Override
    public String getName() {
        return name;
    }

}
