package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * Calculates the standard mean along dimension x of the accelerometer signals
 * 
 * @author christiaan, Elena
 * 
 */
public class FirstXFeatureExtractor extends FeatureExtractor {
    private final String name = "first_x";

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix x = dataForInstances.getX();
        if (x.columns ==0){
            return new DoubleMatrix(x.rows, 1);
        }
        return x.getColumn(0);

    }

    @Override
    public String getName() {
        return name;
    }
}
