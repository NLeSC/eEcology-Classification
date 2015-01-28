package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FormattedSegments;

import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 * Calculates the mean roll feature for the double matrices x, y, z comprising 3-axial data..
 *
 * @author Elena Ranguelova
 * @since July 2014
 */

public class RollFeatureExtractor extends FeatureExtractor {

    /**
     * Calculates the mean roll feature using DoubleMatrix type.
     *
     * <p>
     * The formula for the roll is: roll = (atan(y./sqrt(x.^2+z.^2))*180)/pi.
     *
     */

    @Override
    public DoubleMatrix extractFeatures(FormattedSegments dataForInstances) {
        DoubleMatrix x = dataForInstances.getX().dup();
        DoubleMatrix y = dataForInstances.getY().dup();
        DoubleMatrix z = dataForInstances.getZ().dup();
        DoubleMatrix rollRadians = calculateRollInDegrees(x, y, z);
        DoubleMatrix rollDegrees = radiansToDegrees(rollRadians);
        return rollDegrees;
    }

    private DoubleMatrix calculateRollInDegrees(DoubleMatrix x, DoubleMatrix y, DoubleMatrix z) {
        DoubleMatrix normalToY = calculateEuclidianLength(x, z);

        int rows = x.rows;
        int cols = x.columns;
        DoubleMatrix rollRadians = new DoubleMatrix(rows, cols);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rollRadians.put(r, c, Math.atan2(y.get(r, c), normalToY.get(r, c)));
            }
        }
        return rollRadians;
    }

    private DoubleMatrix calculateEuclidianLength(DoubleMatrix x, DoubleMatrix z) {
        DoubleMatrix xSquare = MatrixFunctions.pow(x, 2);
        DoubleMatrix zSquare = MatrixFunctions.pow(z, 2);
        DoubleMatrix xzSumSquare = xSquare.add(zSquare);
        DoubleMatrix normalToY = MatrixFunctions.sqrt(xzSumSquare);
        return normalToY;
    }

    private DoubleMatrix radiansToDegrees(DoubleMatrix rollRadians) {
        double factor = 180 / Math.PI;
        DoubleMatrix rollDegrees = rollRadians.mul(factor);
        return rollDegrees;
    }

    @Override
    public String getName() {
        return "roll";
    }

}
