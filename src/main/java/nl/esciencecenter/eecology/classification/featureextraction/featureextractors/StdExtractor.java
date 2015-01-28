package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import org.jblas.DoubleMatrix;

/**
 * Calculates the standard deviation of each row of a double matrix.
 *
 * @author Christiaan Meijer, Elena Ranguelova
 * @since July 2014
 *
 */
public class StdExtractor {

    /**
     * Calculates the standard deviation of each row.
     *
     * @param matrix
     *            the input double matrix
     * @return std double matrix of standard deviations
     */
    public DoubleMatrix extractStd(DoubleMatrix matrix) {
        if (matrix.columns - 1 <= 0) {
            // Unable to extract std in correct way. Return 0 as default behavior.
            return new DoubleMatrix(matrix.rows, 1);
        }

        DoubleMatrix rowVariances = getRowVariances(matrix);
        DoubleMatrix std = rowVariances.dup();
        for (int i = 0; i < rowVariances.rows; i++) {
            double variance = rowVariances.get(i, 0);
            double sqrt = Math.sqrt(variance);
            std.put(i, 0, sqrt);
        }
        return std;
    }

    /**
     * Calculates the variance per row of a double matrix
     *
     * @param matrix
     *            input double matrix
     * @return variance double matrix with row variances
     */
    private DoubleMatrix getRowVariances(DoubleMatrix matrix) {
        DoubleMatrix rowMeans = matrix.rowMeans();
        DoubleMatrix difference = matrix.subColumnVector(rowMeans);
        DoubleMatrix differenceSqrd = difference.mul(difference);
        DoubleMatrix variance = differenceSqrd.rowSums().div(matrix.columns - 1);
        return variance;
    }
}
