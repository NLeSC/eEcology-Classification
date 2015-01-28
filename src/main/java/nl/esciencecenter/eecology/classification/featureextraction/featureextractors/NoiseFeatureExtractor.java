package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

public abstract class NoiseFeatureExtractor extends FeatureExtractor {
    protected DoubleMatrix calculateNoise(DoubleMatrix m) {
        if (m.columns <= 2) {
            return new DoubleMatrix(m.rows, 1); // not enough data to have noise => zero noise
        }
        DoubleMatrix before = m.getRange(0, m.rows, 0, m.columns - 2);
        DoubleMatrix center = m.getRange(0, m.rows, 1, m.columns - 1);
        DoubleMatrix after = m.getRange(0, m.rows, 2, m.columns - 0);
        DoubleMatrix noise = center.sub(before.add(after).mul(0.5));
        return MatrixFunctions.abs(noise).rowMeans();
    }

}