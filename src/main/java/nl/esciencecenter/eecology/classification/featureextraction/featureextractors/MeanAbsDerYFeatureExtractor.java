package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

public class MeanAbsDerYFeatureExtractor extends MeanAbsDerFeatureExtractor {

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        return calculateMeanAbsDer(formattedSegments.getY());
    }

    @Override
    public String getName() {
        return "meanabsder_y";
    }

}
