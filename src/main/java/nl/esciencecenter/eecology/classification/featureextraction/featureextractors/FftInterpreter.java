package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import com.google.inject.Inject;

/**
 * Calculates the fundamental frequency from the FFT of a signal
 *
 * @author Elena Ranguelova
 * @since July 2014
 */
public class FftInterpreter {
    @Inject
    private StdExtractor stdExtractor;

    /** the signal's sample rate in Hz */
    private double sampleRate;
    /** the length of the signal */
    private int signalLength;
    /** the number of possible frequencies in the signal */
    private int numFrequencies;
    /** the array index of the fundamental frequency */
    private int fundFreqIndex;
    /** the fundamental frequency in Hz */
    private double fundFrequency;
    /** the signal's spectral magnitude */
    private double[] magnitudes;
    /** the input array -FFT of a signal */
    private double[] input;
    private double maxMagnitude;

    private int indexLowerLimit;

    public void setStdExtractor(StdExtractor stdExtractor) {
        this.stdExtractor = stdExtractor;
    }

    /**
     * Class constructor.
     * <p>
     * The length of the FFT is twice the length of the original signal.
     *
     * @param complexFFTvector
     *            the input complex FFT vector from a signal
     * @param sampleRate
     *            the signal sample rate
     */
    public void constructFftInterpreter(double[] complexFFTvector, int sampleRate) {
        signalLength = complexFFTvector.length / 2;
        numFrequencies = signalLength / 2;
        double defaultFundFrequencyLowerLimit = 1;
        constructFundFreqExtractor(complexFFTvector, sampleRate, numFrequencies, defaultFundFrequencyLowerLimit);
    }

    /**  */
    /**
     * Class constructor.
     * <p>
     * The length of the FFT is twice the length of the original signal.
     *
     * @param complexFFTvector
     *            the input complex FFT vector from a signal
     * @param sampleRate
     *            the signal sample rate
     * @param numFrequencies
     *            the number of bins used to find frequencies with.
     * @param fundFrequencyLowerLimit
     *            fundamental frequency filter upper limit in Hz
     */
    public void constructFundFreqExtractor(double[] complexFFTvector, int sampleRate, int numFrequencies,
            double fundFrequencyLowerLimit) {
        signalLength = complexFFTvector.length / 2;
        this.numFrequencies = numFrequencies;
        this.sampleRate = sampleRate;
        fundFreqIndex = 0;
        fundFrequency = 0.0;
        indexLowerLimit = getIndexFromFrequency(fundFrequencyLowerLimit);
        input = complexFFTvector.clone();
        magnitudes = new double[this.numFrequencies];
    }

    public int GetFundFreqIndex() {
        return fundFreqIndex;
    }

    public double getFundFreq() {
        extractFundFreqAndMagnitude();
        return fundFrequency;
    }

    /**
     * Gets the magnitude of the fundamental frequency.
     *
     * @return
     */
    public double getMaxMagnitude() {
        extractFundFreqAndMagnitude();
        return maxMagnitude;
    }

    /**
     * Extracts the fundamental frequency
     */
    private void extractFundFreqAndMagnitude() {
        calculateMagnitude();
        findMaxMagnitudeIndex();
        fundFrequency = getFrequencyFromIndex(fundFreqIndex);
    }

    /**
     * Calculates the spectral magnitude of the FFT input.
     */
    private void calculateMagnitude() {
        for (int i = 0; i < numFrequencies; i++) {
            double realPart = input[2 * i];
            double imagPart = input[2 * i + 1];
            magnitudes[i] = (realPart * realPart + imagPart * imagPart) / numFrequencies;
        }
    }

    /**
     * Finds the index of the maximum magnitude in the magnitude spectrum.
     */
    private void findMaxMagnitudeIndex() {
        maxMagnitude = Double.NEGATIVE_INFINITY;
        // cut off the below the lower limit as we are not interested in those frequencies
        for (int i = indexLowerLimit; i < numFrequencies; i++) {
            if (magnitudes[i] > maxMagnitude) {
                maxMagnitude = magnitudes[i];
                fundFreqIndex = i;
            }
        }
    }

    private double getFrequencyFromIndex(int fundFreqIndex) {
        return fundFreqIndex * sampleRate / signalLength;

    }

    private int getIndexFromFrequency(double frequency) {
        return (int) Math.round(frequency * signalLength / sampleRate);
    }
}
