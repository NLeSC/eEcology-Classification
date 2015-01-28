package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * This class calculates the correlation between a signal and a sine wave with the frequency equal to the fundamental frequency of
 * the signal. To control for phase shifts between the sequences, the maximum of a number of correlations is taken, sampling phase
 * shifts in the period of the fundamental frequency.
 *
 * @author Christiaan Meijer
 *
 */
public class FundFreqCorrelationExtractor {
    @Inject
    private FundFreqExtractor fundFreqExtractor;

    /**
     * The number of steps used to scan the complete period of the signal by shifting the phase of the sine wave.
     */
    static final int nPhaseShiftSteps = 20;

    public void setFundFreqFeatureExtractor(FundFreqExtractor fundFreqExtractor) {
        this.fundFreqExtractor = fundFreqExtractor;
    }

    public DoubleMatrix extractFundFreqCorrelation(DoubleMatrix input, double sampleFrequency) {
        DoubleMatrix result = new DoubleMatrix(input.rows, 1);
        for (int i = 0; i < input.rows; i++) {
            DoubleMatrix inputRow = input.getRow(i);
            double fundamentalFrequency = getFundamentalFrequency(inputRow);
            double coefficient = getMaxCoefficient(sampleFrequency, inputRow, fundamentalFrequency);
            result.put(i, 0, coefficient);
        }
        return result;
    }

    private double getMaxCoefficient(double sampleFrequency, DoubleMatrix inputRow, double fundamentalFrequency) {
        if (fundamentalFrequency == 0.0) {
            return 0.0; // No way to calculate, so return default value.
        }

        double period = 1 / fundamentalFrequency;
        double step = period / nPhaseShiftSteps;
        double maxCoefficient = 0;
        for (int i = 0; i < nPhaseShiftSteps; i++) {
            DoubleMatrix sine = getSine(fundamentalFrequency, inputRow.length, sampleFrequency, i * step);
            double coefficient = getCorrelation(inputRow, sine);
            maxCoefficient = maxCoefficient > coefficient ? maxCoefficient : coefficient;
        }
        return maxCoefficient;
    }

    private double getCorrelation(DoubleMatrix inputRow, DoubleMatrix sine) {
        if (inputRow.length < 2) {
            return 0;
        }
        double correlation = new PearsonsCorrelation().correlation(inputRow.toArray(), sine.toArray());
        return Double.isNaN(correlation) ? 0 : correlation;
    }

    private DoubleMatrix getSine(double frequency, int length, double sampleFrequency, double phaseShift) {
        DoubleMatrix sine = new DoubleMatrix(1, length);
        for (int i = 0; i < length; i++) {
            double phase = (2 * Math.PI * frequency) * (i / (sampleFrequency) + phaseShift);
            double value = Math.sin(phase);
            sine.put(i, value);
        }
        return sine;
    }

    private double getFundamentalFrequency(DoubleMatrix input) {
        DoubleMatrix frequency = fundFreqExtractor.extractFrequency(input);
        return frequency.get(0, 0);

    }
}
