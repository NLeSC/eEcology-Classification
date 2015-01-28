package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;

import org.jblas.DoubleMatrix;

public abstract class NoisePerAbsDerFeatureExtractor extends FeatureExtractor {

    protected DoubleMatrix extractFeatures(DoubleMatrix m, DoubleMatrix noise, DoubleMatrix derivative) {
        if (m.columns <= 2) {
            return new DoubleMatrix(m.rows, 1);
        }
        return saveDivide(noise, derivative);
    }

    private DoubleMatrix saveDivide(DoubleMatrix noise, DoubleMatrix derivative) {
        DoubleMatrix noisePerDer = noise.div(derivative);
        for (int i = 0; i < noisePerDer.rows; i++) {
            if (Double.isNaN(noisePerDer.get(i, 0))) {
                noisePerDer.put(i, 0, 0d);
            }
        }
        return noisePerDer;
    }

}