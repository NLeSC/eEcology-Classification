package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public abstract class MeanAbsDerFeatureExtractor extends FeatureExtractor {

    public MeanAbsDerFeatureExtractor() {
        super();
    }

    protected DoubleMatrix calculateMeanAbsDer(DoubleMatrix m) {
        if (m.columns <= 1) {
            return new DoubleMatrix(m.rows, 1); // not enough data to have derivative => result zero 
        }
        DoubleMatrix derivative = m.getRange(0, m.rows, 1, m.columns).sub(m.getRange(0, m.rows, 0, m.columns - 1));
        DoubleMatrix absoluteDerivative = MatrixFunctions.abs(derivative);
        return absoluteDerivative.rowMeans();
    }

}