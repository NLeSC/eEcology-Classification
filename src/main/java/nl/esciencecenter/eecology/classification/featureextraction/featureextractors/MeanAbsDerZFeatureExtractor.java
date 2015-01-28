package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

public class MeanAbsDerZFeatureExtractor extends MeanAbsDerFeatureExtractor {

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        return calculateMeanAbsDer(formattedSegments.getZ());
    }

    @Override
    public String getName() {
        return "meanabsder_z";
    }

}
