package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import org.jblas.DoubleMatrix;

/**
 * Convolves two matrices.
 * 
 * @author Christiaan Mejer, Elena Ranguelova- Javadocs
 * @since July 2014
 */

public class Convolver {
    /** Input matrix a. The result of the convolution with be the same dimension as a. */
    private DoubleMatrix a;
    /** Input matrix b. */
    private DoubleMatrix b;
    /** The center column of the matrix b */
    private int centerb;

    /**
     * Conlvoles matrix a with matrix b. The result has the same dimensions as a. This corresponds with setting 'same' in Matlab.
     *
     * @param a
     * @param b
     * @return
     */
    public DoubleMatrix convolve(DoubleMatrix a1, DoubleMatrix b1) {
        a = a1;
        b = b1;
        DoubleMatrix result = new DoubleMatrix(a.rows, a.columns);
        setCenterb();
        for (int aColumni = 0; aColumni < result.columns; aColumni++) {
            calculateConvolutionForColumn(result, aColumni);
        }
        return result;
    }

    /** Sets the center column of matrix b */
    protected void setCenterb() {
        if (b.columns % 2 == 0) { // even
            centerb = b.columns / 2;
        } else { // odd
            centerb = ((b.columns - 1) / 2);
        }
    }

    /**
     * Calculates the convolution for columns
     *
     * @param result
     *            the current result of the convolution
     * @param aColumni
     *            the column to be conlvoved with
     */
    protected void calculateConvolutionForColumn(DoubleMatrix result, int aColumni) {
        DoubleMatrix targetColumn = result.getColumn(aColumni);
        for (int bColumni = 0; bColumni < b.columns; bColumni++) {
            DoubleMatrix currentComponent = calculateCurrentComponent(aColumni, bColumni);
            targetColumn.addi(currentComponent);
        }
        result.putColumn(aColumni, targetColumn);
    }

    /**
     * Calculates the current convolved component
     *
     * @param aColumni
     *            first input column
     * @param bColumni
     *            second input column
     * @return currentComponent convolution of both input columns
     */
    protected DoubleMatrix calculateCurrentComponent(int aColumni, int bColumni) {
        int offseta = bColumni - centerb;
        int cursora = aColumni - offseta;
        int cursorb = bColumni;
        DoubleMatrix currentComponent;
        if (cursora >= 0 && cursora < a.columns) {
            DoubleMatrix columnb = b.getColumn(cursorb);
            DoubleMatrix columna = a.getColumn(cursora);
            currentComponent = columnb.mul(columna);
        } else {
            currentComponent = new DoubleMatrix(a.rows, 1);
        }
        return currentComponent;
    }

}
