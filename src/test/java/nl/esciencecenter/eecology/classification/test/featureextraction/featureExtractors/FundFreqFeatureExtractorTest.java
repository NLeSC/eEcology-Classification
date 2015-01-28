package nl.esciencecenter.eecology.classification.test.featureextraction.featureExtractors;

import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.ArrayMeanRemover;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FftInterpreter;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.HammingWindowing;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.WindowedFFTExtractor;

public abstract class FundFreqFeatureExtractorTest extends FeatureExtractorTest {

    public FundFreqFeatureExtractorTest() {
        super();
    }

    protected FundFreqExtractor getFundFreqExtractor() {
        FundFreqExtractor fundFreqExtractor = new FundFreqExtractor();
        fundFreqExtractor.setArrayMeanRemover(new ArrayMeanRemover());
        FftInterpreter fftInterpreter = new FftInterpreter();
        fftInterpreter.setStdExtractor(new StdExtractor());
        fundFreqExtractor.setFftInterpreter(fftInterpreter);
        WindowedFFTExtractor winFFTExtractor = new WindowedFFTExtractor();
        winFFTExtractor.setHammingWindowing(new HammingWindowing());
        winFFTExtractor.setMinBins(0);
        fundFreqExtractor.setWindowedFFTExtractor(winFFTExtractor);
        fundFreqExtractor.setSampleFrequency(20);
        return fundFreqExtractor;
    }

}