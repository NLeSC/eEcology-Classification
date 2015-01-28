package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

public class StepResponseFeatureExtractor extends FeatureExtractor {
    @Inject
    private MeanExtractor meanExtractor;
    @Inject
    private Convolver convolver;

    private final DoubleMatrix filter = new DoubleMatrix(new double[][] { { -0.0667, 0.1463, 0.3886, 0.4430, 0.3763, 0.3213,
        0.2795, 0.2016, 0.0878, -0.0424, -0.1720, -0.2821, -0.3319, -0.2668 } }); // smoothed average vulture step.

    public void setConvolver(Convolver convolver) {
        this.convolver = convolver;
    }

    public void setMeanExtractor(MeanExtractor meanExtractor) {
        this.meanExtractor = meanExtractor;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix meanX = meanExtractor.extractMean(formattedSegments.getX());
        DoubleMatrix normalized = formattedSegments.getX().subColumnVector(meanX);
        DoubleMatrix filterMatrix = new DoubleMatrix(normalized.rows, filter.columns).addRowVector(filter);
        DoubleMatrix convolved = convolver.convolve(normalized, filterMatrix);
        DoubleMatrix positive = convolved.max(new DoubleMatrix(convolved.rows, convolved.columns));
        return positive.rowSums();
    }

    @Override
    public String getName() {
        return "stepresponse";
    }

}
