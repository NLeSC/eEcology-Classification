package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

import com.google.inject.Inject;

public class VedbaFeatureExtractor extends FeatureExtractor {

    private final Convolver convolver;

    @Inject
    public VedbaFeatureExtractor(Convolver convolver) {
        this.convolver = convolver;
    }

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments formattedSegments) {
        DoubleMatrix xComponent = calculateComponent(formattedSegments.getX());
        DoubleMatrix yComponent = calculateComponent(formattedSegments.getY());
        DoubleMatrix zComponent = calculateComponent(formattedSegments.getZ());
        DoubleMatrix sum = xComponent.add(yComponent).add(zComponent);
        return MatrixFunctions.sqrt(sum);
    }

    private DoubleMatrix calculateComponent(DoubleMatrix m) {
        DoubleMatrix convolved = getConvolved(m);
        DoubleMatrix difference = m.sub(convolved);
        DoubleMatrix squaredDifference = difference.mul(difference);
        return squaredDifference.rowSums();
    }

    private DoubleMatrix getConvolved(DoubleMatrix m) {
        int windowColumns = (int) Math.floor(m.columns / 2d);
        DoubleMatrix window = new DoubleMatrix(m.rows, windowColumns).add(1d / windowColumns);
        DoubleMatrix convolved = convolver.convolve(m, window);
        return convolved;
    }

    @Override
    public String getName() {
        return "vedba";
    }

}
