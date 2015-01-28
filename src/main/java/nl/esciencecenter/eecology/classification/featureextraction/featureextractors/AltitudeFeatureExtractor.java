package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

public class AltitudeFeatureExtractor extends FeatureExtractor {

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix altitude = formattedSegments.getAltitude();
        if (altitude.columns == 0) {
            return new DoubleMatrix(altitude.rows, 1);
        }
        int middleColumn = (int) (Math.ceil(altitude.columns / 2d) - 1);
        return altitude.getColumn(middleColumn);
    }

    @Override
    public String getName() {
        return "altitude";
    }

}
