package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

/**
 * Performs Hamming windowing on a double vector
 *
 * @author Elena Ranguelova
 * @since July 2014
 *
 */
public class HammingWindowing {
    /** the Hamming window size */
    private int size;
    /** half of the window size */
    private int half;
    /** the array containing the Hamming window coefficients */
    private double[] window; // the window data

    /**
     * Computes the Hamming window coefficients according to the standart formula
     * 
     * @param winSize
     *            the Hamming window size
     */
    public void constructWindow(int winSize) {
        size = winSize;
        window = new double[size];
        if (size % 2 != 0) // odd length
        {
            half = (size + 1) / 2;
        } else // even length
        {
            half = size / 2;
        }
        for (int i = 0; i < half; i++) {
            window[i] = (0.54 - 0.46 * Math.cos(2 * Math.PI * i / (size - 1)));
        }
        for (int i = size - 1; i > 0; i--) {
            window[i] = window[size - (i + 1)];
        }
    }

    /**
     * getter for the Hamming window
     *
     * @return window
     */
    public double[] GetWindow() {
        return window;
    }

    /**
     * Applies Hamming window to an input vector
     * 
     * @param vector
     *            the input double vector
     * @return windowedVector the vector multiplied element-wise with the Hamming window coefficient
     */
    public double[] doHammingWindowing(double[] vector) {
        int vectorSize = vector.length;
        double[] windowedVector = new double[vectorSize];

        // multiply the input vector by the Hamming window coefficients
        if (vectorSize > 1) {
            for (int i = 0; i < vectorSize; i++) {
                windowedVector[i] = window[i] * vector[i];
            }
        } else if (vectorSize > 0) {
            windowedVector[0] = 1;
        }

        return windowedVector;
    }
}
