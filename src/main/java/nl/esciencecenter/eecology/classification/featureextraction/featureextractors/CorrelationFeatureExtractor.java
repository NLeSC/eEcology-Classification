package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jblas.DoubleMatrix;

/**
 * Calculates the correlations between two sets of sequences and returns the correlations.
 * 
 * @author Christiaan Meijer, Elena Ranguelova- Javadoc
 * @since July 2014
 * @see org.apache.commons.math3.stat.correlation.PearsonsCorrelation
 * 
 */
public abstract class CorrelationFeatureExtractor extends FeatureExtractor {

    protected DoubleMatrix getCorrelations(int numberOfInstances, DoubleMatrix sequencesA, DoubleMatrix sequencesB) {
        DoubleMatrix result = new DoubleMatrix(numberOfInstances, 1);
        for (int r = 0; r < numberOfInstances; r++) {
            double[] sequenceA = sequencesA.getRow(r).data;
            double[] sequenceB = sequencesB.getRow(r).data;
            result.put(r, 0, getCorrelation(sequenceA, sequenceB));
        }
        return result;
    }

    /** Calculates the correlation between two double arrays
     * 
     * @param a first dobule array
     * @param b second double array
     * @return correlation Pearson't correlation between arrays a and b
     */
    private double getCorrelation(double[] a, double[] b) {
        double correlation;
        if (a.length >= 2) {
            correlation = new PearsonsCorrelation().correlation(a, b);
        } else {
            correlation = 1;
        }

        if (Double.isNaN(correlation)) {
            // No correlation defined mathematically means variables have zero std. For discussion about implications:
            // http://stats.stackexchange.com/questions/18333/what-is-the-correlation-if-the-standard-deviation-of-one-variable-is-0
            correlation = 0;
        }
        return correlation;
    }

}
