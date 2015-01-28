package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.ArrayMeanRemover;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FftInterpreter;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.HammingWindowing;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.WindowedFFTExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class FftInterpreterTest {
    protected WindowedFFTExtractor winFFTExtractor;
    protected FftInterpreter fundFreqExtractor;
    protected ArrayMeanRemover arrayMeanRemoval;

    protected final double errorMargin = 0.0001;
    private final double[] testData = { 1.4485, 1.3303, 1.6070, -0.1430, -1.2012, -0.5990, 1.1866, 1.5933, 1.5290, 0.1198,
            -2.4521, -0.7300, 0.2863, 0.4637, -0.8726, -1.1247, -1.9034, -0.3450, 1.2737, 0.9136 };
    private final double[] matlabFftVector = { 2.3808, 0.0, 4.9246, -5.547, 5.2657, 2.7993, 10.4351, -0.7862, -8.1533, -0.8894,
            -0.5834, -0.4606, -3.1769, 0.6227, 2.239, -1.0347, 0.1446, -0.5257, 2.4876, 1.5582, -0.5772, 0.0, 2.4876, -1.5582,
            0.1446, 0.5257, 2.239, 1.0347, -3.1769, -0.6227, -0.5834, 0.4606, -8.1533, 0.8894, 10.4351, 0.7862, 5.2657, -2.7993,
            4.9246, 5.547 };

    /*- Matlab code to generate max z score:
    fs = 20;                                        % Sample frequency (Hz)
    x = [1.4485, 1.3303, 1.6070, -0.1430, -1.2012, -0.5990, 1.1866, 1.5933, 1.5290, 0.1198, -2.4521, -0.7300, 0.2863, 0.4637, -0.8726, -1.1247, -1.9034, -0.3450, 1.2737, 0.9136];
    m = length(x);                                  % Window length
    n=20;                                           % overrule with number 2^something
    y = fft(x,n);                                   % DFT
    f = (0:n-1)*(fs/n);                             % Frequency range
    power = y.*conj(y)/n;                           % Power of the DFT
    Mean = mean(power(1:L))
    Std = std(power(1:L))
    L=floor(length(f))/2;
    Max=max(power(1:L));
    z = (Max - Mean) / Std
     */
    private final double matlabMaxZscoreWithoutLowerLimit = 2.2231;

    /*- Matlab code to generate max z score:
    lowerLimit = 6;
    fs = 20;                                        % Sample frequency (Hz)
    x = [1.4485, 1.3303, 1.6070, -0.1430, -1.2012, -0.5990, 1.1866, 1.5933, 1.5290, 0.1198, -2.4521, -0.7300, 0.2863, 0.4637, -0.8726, -1.1247, -1.9034, -0.3450, 1.2737, 0.9136];
    m = length(x);                                  % Window length
    n=20;                                           % overrule with number 2^something
    y = fft(x,n);                                   % DFT
    f = (0:n-1)*(fs/n);                             % Frequency range
    power = y.*conj(y)/n;                           % Power of the DFT
    L=floor(length(f))/2;
    Mean = mean(power(lowerLimit:L)
    Std = std(power(lowerLimit:L))
    Max=max(power(lowerLimit:L));
    z = (Max - Mean) / Std
     */
    private final double matlabMaxZscoreWithLowerLimit = 2.7464;

    @Test
    public void fundFreqExtractorTest_zeroInputZeroIndex() {
        // Arrange
        int frequency = 20;
        double[] input = new double[1];
        input[0] = 0;

        // Act
        prepareInputAndGiveToFundFreqExtractor(input, frequency);
        int output = fundFreqExtractor.GetFundFreqIndex();

        // Assert
        assertEquals(0, output);
    }

    @Test
    public void fundFreqExtractor_1sineWithZeroMean_correctOutput() {
        // Arrange
        int frequency = 20;
        double[] input = getSineSequenceWithZeroMeanAndPeriod3();
        double expected = 3;

        // Act
        prepareInputAndGiveToFundFreqExtractor(input, frequency);
        double output = fundFreqExtractor.getFundFreq();

        // Assert
        assertEquals(expected, output, errorMargin);
    }

    @Test
    public void fundFreqExtractor_1sineWithZeroMeanAndRealPeriod_correctOutput() {
        // Arrange
        int frequency = 20;
        double[] input = getSineSequenceWithZeroMeanAndPeriod4_5();
        winFFTExtractor.setMinBins(512);
        double expected = 4.5;
        double largeErrorMargin = 0.1;

        // Act
        prepareInputAndGiveToFundFreqExtractor(input, frequency);
        double output = fundFreqExtractor.getFundFreq();

        // Assert
        assertEquals(expected, output, largeErrorMargin);
    }

    @Test
    public void fundFreqExtractor_sineWithPositiveOffset_correctOutput() {
        // Arrange
        int frequency = 20;
        double[] sine = getSineSequenceWithZeroMeanAndPeriod3();
        int offset = 3;
        double[] input = new DoubleMatrix(sine).add(offset).toArray();
        double expected = 3;

        // Act
        prepareInputAndGiveToFundFreqExtractor(input, frequency);
        double output = fundFreqExtractor.getFundFreq();

        // Assert
        assertEquals(expected, output, errorMargin);
    }

    @Test
    public void getMaxMagnitude_useTestDataDummyWindow_correctMaxMagnitude() {
        // Arrange

        // Act
        fundFreqExtractor.constructFundFreqExtractor(matlabFftVector, 20, 20, 0);
        double maxMagnitude = fundFreqExtractor.getMaxMagnitude();

        // Assert
        assertEquals(5.4755, maxMagnitude, errorMargin);
    }

    @Test
    public void getOutput_useTestDataDummyWindow_correctOutcome() {
        // Arrange
        setDummyHammingWindow();
        winFFTExtractor.setMinBins(20);

        // Act
        winFFTExtractor.constructWindowedFFTExtractor(testData);
        winFFTExtractor.doWindowedFFTExtraction();
        double[] frequencyDomain = winFFTExtractor.getOutput();

        // Assert
        for (int i = 0; i < matlabFftVector.length; i++) {
            assertEquals(matlabFftVector[i], frequencyDomain[i], errorMargin);
        }
    }

    @Test
    public void getFundFreq_actualSequenceResultedInFrequencyBelowLowerLimit_resultNoLongerIsBelowLimit() {
        // Arrange
        double[] input = { 0.004069, -0.027844, -0.011090, -0.026249, -0.035025, -0.051779, -0.079703, -0.072523, -0.067736,
                -0.026249, -0.036620, 0.095022, 0.152465, 0.138104, -0.172251, 0.014441, 0.271342, 0.229057, 0.200335, -0.076512 };
        int frequency = 20;
        winFFTExtractor.setMinBins(512);

        // Act
        prepareInputAndGiveToFundFreqExtractor(input, frequency);
        double fundFreq = fundFreqExtractor.getFundFreq();

        // Assert
        assertTrue(fundFreq >= 1);
    }

    @Ignore
    @Test
    /**
     * This test was created because it seemed that all instances had a value very close to discrete numbers. This tests one such example.
     * 
     * It is failing right now, so TODO
     */
    public void getFundFreq_actualSequenceResultedInDiscretishFrequency_resultNoLongerDiscretish() {
        // Arrange
        double[] input = { 0.302422755209797, 0.321415818942922, 0.214294939488099, 0.347246385619972, 0.272033853236798,
                0.222651887530674, 0.17326992182455, 0.188464372811049, 0.232528280671899, 0.175549089472525, 0.244683841461098,
                0.245443564010423, 0.250761621855698, 0.205937991445524, 0.284189414025998, 0.281150523828698, 0.297104697364522,
                0.334331102281447, 0.272033853236798, 0.294825529716548, 0.302422755209797, 0.321415818942922, 0.214294939488099,
                0.347246385619972, 0.272033853236798, 0.222651887530674, 0.17326992182455, 0.188464372811049, 0.232528280671899,
                0.175549089472525, 0.244683841461098, 0.245443564010423, 0.250761621855698, 0.205937991445524, 0.284189414025998,
                0.281150523828698, 0.297104697364522, 0.334331102281447, 0.272033853236798, 0.294825529716548 };
        int frequency = 20;
        winFFTExtractor.setMinBins(512);
        setDummyHammingWindow();
        double expected = 12;

        // Act
        prepareInputAndGiveToFundFreqExtractor(input, frequency);
        double fundFreq = fundFreqExtractor.getFundFreq();

        // Assert
        assertEquals(expected, fundFreq, errorMargin);
    }

    @Test
    public void extractFeature_constantInput_noNaNOutput() {
        // Arrange
        double[] constant = new double[20];
        for (int i = 0; i < constant.length; i++) {
            constant[i] = 1413;
        }
        int frequency = 20;
        winFFTExtractor.setMinBins(512);

        // Act
        prepareInputAndGiveToFundFreqExtractor(constant, frequency);
        double magnitude = fundFreqExtractor.getMaxMagnitude();

        // Assert
        assertTrue(Double.isNaN(magnitude) == false);
    }

    private void setDummyHammingWindow() {
        winFFTExtractor.setHammingWindowing(new HammingWindowing() {
            @Override
            public double[] doHammingWindowing(double[] vector) {
                return vector;
            }
        });
    }

    private void prepareInputAndGiveToFundFreqExtractor(double[] input, int sampleRate) {
        arrayMeanRemoval.constructArrayMeanRemoval(input);
        arrayMeanRemoval.removeMean();
        double[] zero_mean_input = arrayMeanRemoval.getOutput();

        winFFTExtractor.constructWindowedFFTExtractor(zero_mean_input);
        winFFTExtractor.doWindowedFFTExtraction();
        double[] fft = winFFTExtractor.getOutput();

        fundFreqExtractor.constructFftInterpreter(fft, sampleRate);
    }

    private double getOutput(double[] input, int size) {
        prepareInputAndGiveToFundFreqExtractor(input, size);
        double output = fundFreqExtractor.getFundFreq();
        return output;
    }

    private double[] getSineSequenceWithZeroMeanAndPeriod3() {
        double[] input = new double[20];
        // sine wave values generated by Matlab via
        //        T=1; % duration [s]
        //        Fs = 20; % sample rate [Hz]
        //        n = T*Fs; % samples
        //        t = 0 : 1/Fs : T  - 1/Fs; %samples vector
        //        Fn = 3; %Frequency [Hz]
        //        input = sin(Fn*2*pi*t); %Signal

        input[0] = 0;
        input[1] = 0.809016994374947;
        input[2] = 0.951056516295154;
        input[3] = 0.309016994374947;
        input[4] = -0.587785252292473;
        input[5] = -1;
        input[6] = -0.587785252292473;
        input[7] = 0.309016994374948;
        input[8] = 0.951056516295154;
        input[9] = 0.809016994374948;
        input[10] = 0;
        input[11] = -0.809016994374946;
        input[12] = -0.951056516295154;
        input[13] = -0.309016994374950;
        input[14] = 0.587785252292473;
        input[15] = 1;
        input[16] = 0.587785252292475;
        input[17] = -0.309016994374945;
        input[18] = -0.951056516295153;
        input[19] = -0.809016994374949;
        return input;
    }

    private double[] getSineSequenceWithZeroMeanAndPeriod4_5() {
        // sine wave values generated by Matlab via
        // sin(2* pi *(1:20) / 4.5)'
        double[] input = { 0.9848, 0.3420, -0.8660, -0.6428, 0.6428, 0.8660, -0.3420, -0.9848, -0.0000, 0.9848, 0.3420, -0.8660,
                -0.6428, 0.6428, 0.8660, -0.3420, -0.9848, -0.0000, 0.9848, 0.3420 };
        return input;
    }

    @Before
    public void setUp() throws Exception {
        winFFTExtractor = new WindowedFFTExtractor();
        winFFTExtractor.setHammingWindowing(new HammingWindowing());
        winFFTExtractor.setMinBins(0);
        fundFreqExtractor = new FftInterpreter();
        fundFreqExtractor.setStdExtractor(new StdExtractor());
        arrayMeanRemoval = new ArrayMeanRemover();
    }
}
