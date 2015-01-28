package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

/**
 * Get the speed from the input data
 *
 * @author Elena Ranguelova
 *
 */
public class GpsSpeedFeatureExtractor extends FeatureExtractor {
    private final String name = "gps_speed";

    /**
     * Gets the speed from the input data
     */
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix gpsSpeed = formattedSegments.getGpsSpeed();
        if (gpsSpeed.columns == 0) {
            return new DoubleMatrix(gpsSpeed.rows, 1);
        }
        int middleColumn = (int) (Math.ceil(gpsSpeed.columns / 2d) - 1);
        return gpsSpeed.getColumn(middleColumn);
    }

    @Override
    public String getName() {
        return name;
    }
}
