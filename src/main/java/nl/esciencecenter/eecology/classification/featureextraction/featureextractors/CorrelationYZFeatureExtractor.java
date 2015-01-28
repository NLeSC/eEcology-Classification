package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

public class CorrelationYZFeatureExtractor extends CorrelationFeatureExtractor {

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        return super.getCorrelations(formattedSegments.getNumberOfRows(), formattedSegments.getY(), formattedSegments.getZ());
    }

    @Override
    public String getName() {
        return "correlation_yz";
    }

}
