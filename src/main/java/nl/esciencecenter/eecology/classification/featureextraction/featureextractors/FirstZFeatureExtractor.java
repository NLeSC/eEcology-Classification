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
public class FirstZFeatureExtractor extends FeatureExtractor {
    private final String name = "first_z";

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix z = dataForInstances.getZ();
        if (z.columns ==0){
            return new DoubleMatrix(z.rows, 1);
        }
        return z.getColumn(0);

    }

    @Override
    public String getName() {
        return name;
    }
}
