package nl.esciencecenter.eecology.classification.featureextraction.featureextractors;

import org.jblas.DoubleMatrix;

import com.google.inject.Inject;

/**
 * This class calculates magnitude of the fundamental frequency of the signal in the power spectrum.
 *
 * @author Christiaan Meijer
 *
 */
public class FundFreqMagnitudeExtractor {
    @Inject
    private FundFreqExtractor fundFreqExtractor;

    public void setFundFreqFeatureExtractor(FundFreqExtractor fundFreqExtractor) {
        this.fundFreqExtractor = fundFreqExtractor;
    }

    public DoubleMatrix extractFundFreqMagnitude(DoubleMatrix input, double sampleFrequency) {
        DoubleMatrix result = new DoubleMatrix(input.rows, 1);
        for (int i = 0; i < input.rows; i++) {
            DoubleMatrix inputRow = input.getRow(i);
            double fundamentalFrequency = getFundamentalFrequencyMagnitude(inputRow);
            result.put(i, 0, fundamentalFrequency);
        }
        return result;
    }

    private double getFundamentalFrequencyMagnitude(DoubleMatrix input) {
        DoubleMatrix magnitude = fundFreqExtractor.extractMagnitudes(input);
        return magnitude.get(0, 0);

    }
}
