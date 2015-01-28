package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

public class MeanAbsDerXFeatureExtractor extends MeanAbsDerFeatureExtractor {
    /**
     * Calculates the mean of the absolute derivative of the sequence.
     */
    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        return calculateMeanAbsDer(formattedSegments.getX());
    }

    @Override
    public String getName() {
        return "meanabsder_x";
    }

}
