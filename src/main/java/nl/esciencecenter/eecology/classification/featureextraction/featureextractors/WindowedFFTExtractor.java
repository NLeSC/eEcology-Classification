package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import org.apache.commons.lang3.ArrayUtils;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

/**
 * Calculates the FFT over a Hamming windowed vector
 *
 * @author Elena Ranguelova
 * @since July 2014
 * @see edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D
 */
/** class for computing the Hamming windowing over an input vector */
public class WindowedFFTExtractor {

    @Inject
    private HammingWindowing hammingWindowing;

    @Inject
    @Named("minimum_number_of_bins_used_by_fft")
    private int minBins = 512;

    /** the input vector */
    private double[] inputVector;
    /** the vector'slength */
    private int vectorLength;
    /** the windowed vector */
    private double[] outputVector;
    /** FFT class implemented in jtransforms */
    private DoubleFFT_1D fftDo;

    public void setMinBins(int minBins) {
        this.minBins = minBins;
    }

    public void setHammingWindowing(HammingWindowing hammingWindowing) {
        this.hammingWindowing = hammingWindowing;
    }

    /**
     * class constructor
     *
     * Calculates the Hamming windowing over the input vector. Initializins the FFT contrainer
     *
     * @param input
     *            the input data vector
     */
    public void constructWindowedFFTExtractor(double[] input) {
        int inputLength = input.length;
        // apply the Hamming windowing to the raw input
        hammingWindowing.constructWindow(inputLength);
        double[] windowedInputVector = hammingWindowing.doHammingWindowing(input);
        inputVector = ArrayUtils.addAll(windowedInputVector, new double[Math.max(0, minBins - windowedInputVector.length)]);
        vectorLength = inputVector.length;
        outputVector = new double[vectorLength * 2];
        if (vectorLength > 0) {
            fftDo = new DoubleFFT_1D(vectorLength);
        }
    }

    public double[] getOutput() {
        return outputVector;
    }

    /** Calculates the complex FFT over an input vector */
    //perform FFT
    public void doWindowedFFTExtraction() {
        if (vectorLength > 0) {
            // copy  the input vector in the proper parts of the output vector
            for (int i = 0; i < vectorLength; i++) {
                outputVector[2 * i] = inputVector[i];
                outputVector[2 * i + 1] = 0;
            }
            // apply the complex FFT
            fftDo.complexForward(outputVector);
        }
    }

}
