package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 * Calculates the pitch feature for the double matrices x, y, z comprising 3-axial data.
 *
 * @author Elena Ranguelova
 * @since July 2014
 */

public class PitchFeatureExtractor extends FeatureExtractor {

    /**
     * Calculates the pitch feature using DoubleMatrix type.
     * <p>
     * The formula for the pitch is pitch = (atan(x./sqrt(y.^2+z.^2))*180)/pi.
     *
     */

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix x = dataForInstances.getX().dup();
        DoubleMatrix y = dataForInstances.getY().dup();
        DoubleMatrix z = dataForInstances.getZ().dup();
        DoubleMatrix pitchRadians = calculatePitchInRadians(x, y, z);
        DoubleMatrix pitchDegrees = radiansToDegrees(pitchRadians);
        return pitchDegrees;
    }

    private DoubleMatrix calculatePitchInRadians(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z) {
        DoubleMatrix normalToX = calculateEuclidianLength(y, z);

        int rows = x.rows;
        int cols = x.columns;
        DoubleMatrix pitchRadians = new DoubleMatrix(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                pitchRadians.put(r, c, Math.atan2(x.get(r, c), normalToX.get(r, c)));
            }
        }
        return pitchRadians;
    }

    private DoubleMatrix calculateEuclidianLength(DoubleMatrix y, DoubleMatrix z) {
        DoubleMatrix ySquare = MatrixFunctions.pow(y, 2);
        DoubleMatrix zSquare = MatrixFunctions.pow(z, 2);
        DoubleMatrix yzSumSquare = ySquare.add(zSquare);
        DoubleMatrix normalToX = MatrixFunctions.sqrt(yzSumSquare);
        return normalToX;
    }

    private DoubleMatrix radiansToDegrees(DoubleMatrix pitchRadians) {
        double factor = 180 / Math.PI;
        DoubleMatrix pitchDegrees = pitchRadians.mul(factor);
        return pitchDegrees;
    }

    @Override
    public String getName() {
        return "pitch";
    }

}
