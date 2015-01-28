package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class FundFreqExtractor {
    @Inject
    protected ArrayMeanRemover arrayMeanRemover;
    @Inject
    protected WindowedFFTExtractor windowedFFTExtractor;
    @Inject
    protected FftInterpreter fftInterpreter;

    @Inject
    @Named("accelerometer_sample_frequency")
    private int sampleFrequency;

    private DoubleMatrix fundFrequencies;
    private DoubleMatrix magnitudes;

    public void setSampleFrequency(int sampleFrequency) {
        this.sampleFrequency = sampleFrequency;
    }

    public void setArrayMeanRemover(ArrayMeanRemover arrayMeanRemover) {
        this.arrayMeanRemover = arrayMeanRemover;
    }

    public void setWindowedFFTExtractor(WindowedFFTExtractor windowedFFTExtractor) {
        this.windowedFFTExtractor = windowedFFTExtractor;
    }

    public void setFftInterpreter(FftInterpreter fundFreqExtractor) {
        fftInterpreter = fundFreqExtractor;
    }

    /**
     * Extracts the fundamental frequency of each instance in input.
     *
     * @param input
     * @return
     */
    protected DoubleMatrix extractFrequency(DoubleMatrix input) {
        calculateFundFrequencyAndMagnitude(input);
        return fundFrequencies;
    }

    /**
     * Extracts the magnitude of the fundamental frequency of each instance in input.
     *
     * @param input
     * @return
     */
    protected DoubleMatrix extractMagnitudes(DoubleMatrix input) {
        calculateFundFrequencyAndMagnitude(input);
        return magnitudes;
    }

    private void calculateFundFrequencyAndMagnitude(DoubleMatrix input) {
        int numberOfInstances = input.rows;
        fundFrequencies = new DoubleMatrix(numberOfInstances);
        magnitudes = new DoubleMatrix(numberOfInstances);
        for (int i = 0; i < numberOfInstances; i++) {
            double[] signal = input.getRow(i).data;

            arrayMeanRemover.constructArrayMeanRemoval(signal);
            arrayMeanRemover.removeMean();
            double[] zeroMeanSignal = arrayMeanRemover.getOutput();

            windowedFFTExtractor.constructWindowedFFTExtractor(zeroMeanSignal);
            windowedFFTExtractor.doWindowedFFTExtraction();
            double[] fft = windowedFFTExtractor.getOutput();

            fftInterpreter.constructFftInterpreter(fft, sampleFrequency);
            fundFrequencies.put(i, fftInterpreter.getFundFreq());
            magnitudes.put(i, fftInterpreter.getMaxMagnitude());
        }
    }
}