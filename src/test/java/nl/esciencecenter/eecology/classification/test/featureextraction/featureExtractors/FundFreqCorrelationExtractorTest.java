package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import static org.junit.Assert.assertEquals;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqCorrelationExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqExtractor;

import org.jblas.DoubleMatrix;
import org.junit.Before;
import org.junit.Test;

public class FundFreqCorrelationExtractorTest {
    private final double strictErrorMargin = 0.0001;
    private final double looseErrorMargin = 0.05;
    private FundFreqCorrelationExtractor periodicityExtractor;
    private final double sampleFrequency = 20;

    @Test
    public void extractFundFreqCorrelation_noSignals_returnNoRows() {
        // Arrange
        DoubleMatrix input = new DoubleMatrix(0, 0);

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(0, result.rows, strictErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_3Signals_return3Rows() {
        // Arrange
        DoubleMatrix input = new DoubleMatrix(3, 1);

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(3, result.rows, strictErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_zeroSignal_returnZero() {
        // Arrange
        DoubleMatrix input = new DoubleMatrix(1, 1);

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(0, result.get(0, 0), strictErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_sineWave_return1() {
        // Arrange
        DoubleMatrix input = getSineWithPhaseShift(0);

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(1, result.get(0, 0), strictErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_sineWaveWithNonZeroMean_return1() {
        // Arrange
        DoubleMatrix input = getSineWithPhaseShift(0);
        input.add(5);

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(1, result.get(0, 0), strictErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_sineWaveWithPhaseShift_return1() {
        // Arrange
        DoubleMatrix input = getSineWithPhaseShift(0.1111);

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(1, result.get(0, 0), looseErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_zeroFundFreq_return0() {
        // Arrange
        DoubleMatrix input = getSineWithPhaseShift(0.1111);
        periodicityExtractor.setFundFreqFeatureExtractor(getDummyFundFreqExtractorReturning0());

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(0, result.get(0, 0), strictErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_zeros_return0() {
        // Arrange
        DoubleMatrix input = new DoubleMatrix(new double[][] { { 0, 0, 0, 0, 0, 0, 0 } });

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(0, result.get(0, 0), strictErrorMargin);
    }

    @Test
    public void extractFundFreqCorrelation_constant_return0() {
        // Arrange
        DoubleMatrix input = new DoubleMatrix(new double[][] { { 4, 4, 4, 4, 4, 4, 4 } });

        // Act
        DoubleMatrix result = periodicityExtractor.extractFundFreqCorrelation(input, sampleFrequency);

        // Assert
        assertEquals(0, result.get(0, 0), strictErrorMargin);
    }

    @Before
    public void SetUp() {
        periodicityExtractor = new FundFreqCorrelationExtractor();
        periodicityExtractor.setFundFreqFeatureExtractor(getDummyFundFrequencyFeatureExtractorReturning3());
    }

    private DoubleMatrix getSineWithPhaseShift(double phaseShift) {
        int frequency = 3;
        DoubleMatrix input = new DoubleMatrix(1, 20);
        for (int i = 0; i < input.columns; i++) {
            double phase = (2 * Math.PI * frequency) * (phaseShift + (i / sampleFrequency));
            input.put(0, i, Math.sin(phase));
        }
        return input;
    }

    private FundFreqExtractor getDummyFundFreqExtractorReturning0() {
        return new FundFreqExtractor() {
            @Override
            protected DoubleMatrix extractFrequency(DoubleMatrix input) {
                DoubleMatrix zeros = new DoubleMatrix(input.rows, 1);
                return zeros;
            }
        };
    }

    private FundFreqExtractor getDummyFundFrequencyFeatureExtractorReturning3() {
        return new FundFreqExtractor() {
            @Override
            public DoubleMatrix extractFrequency(DoubleMatrix input) {
                return new DoubleMatrix(new double[] { 3 });
            }
        };
    }
}
