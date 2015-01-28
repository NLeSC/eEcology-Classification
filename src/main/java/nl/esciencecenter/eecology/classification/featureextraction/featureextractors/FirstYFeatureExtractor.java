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
public class FirstYFeatureExtractor extends FeatureExtractor {
    private final String name = "first_y";
    
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix y = dataForInstances.getY();
        if (y.columns ==0){
            return new DoubleMatrix(y.rows, 1);
        }
        return y.getColumn(0);

    }

    @Override
    public String getName() {
        return name;
    }
}
