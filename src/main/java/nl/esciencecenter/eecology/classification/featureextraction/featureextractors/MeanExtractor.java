package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import org.jblas.DoubleMatrix;

/**
 * Calculates the standard mean of each row.
 *
 * @author Christiaan Meijer, Elena Ranguelova
 *
 */
public class MeanExtractor {
    /**
     * Calculates the mean of each row.
     * 
     * @return mean
     */
    public DoubleMatrix extractMean(DoubleMatrix matrix) {
        DoubleMatrix mean = matrix.rowMeans();
        return mean;
    }
}
