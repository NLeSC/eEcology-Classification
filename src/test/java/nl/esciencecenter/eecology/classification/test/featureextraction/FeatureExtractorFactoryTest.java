package nl.esciencecenter.eecology.classification.test.featureextraction;

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.dataaccess.CustomFeatureExtractorLoader;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractorFactory;
import nl.esciencecenter.eecology.classification.featureextraction.InvalidCustomFeatureExtractorNameException;
import nl.esciencecenter.eecology.classification.featureextraction.UnknownFeatureExtractorTypeException;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.AltitudeFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CompositeFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CorrelationXYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CorrelationXZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CorrelationYZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CustomFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FirstXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FirstYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FirstZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqCorrelationXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqCorrelationYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqCorrelationZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqMagnitudeXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqMagnitudeYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqMagnitudeZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.FundFreqZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.GpsSpeedFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanAbsDerXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanAbsDerYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanAbsDerZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanLocationXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanLocationYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanLocationZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanPitchFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanRollFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeasurementClassifierFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoisePerAbsDerXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoisePerAbsDerYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoisePerAbsDerZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoiseXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoiseYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.NoiseZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.OdbaFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.RawInputFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdLocationXFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdLocationYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdLocationZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdPitchFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdRollFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StepResponseFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.VedbaFeatureExtractor;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class FeatureExtractorFactoryTest {
    @Test
    public void getFeatureExtractor_noFeaturesExtractors_0childExtractors() {
        // Arrange
        List<String> features = new LinkedList<String>();
        FeatureExtractorFactory featureExtractorFactory = getTestFeatureExtractorFactory(features);

        // Act
        FeatureExtractor fe = featureExtractorFactory.getFeatureExtractor();
        int childCount = ((CompositeFeatureExtractor) fe).getChildFeatureExtractors().size();

        // Assert
        assertEquals(0, childCount);
    }

    @Test
    public void getFeatureExtractor_null_0childExtractors() {
        // Arrange
        List<String> features = null;
        FeatureExtractorFactory featureExtractorFactory = getTestFeatureExtractorFactory(features);

        // Act
        FeatureExtractor fe = featureExtractorFactory.getFeatureExtractor();
        int childCount = ((CompositeFeatureExtractor) fe).getChildFeatureExtractors().size();

        // Assert
        assertEquals(0, childCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFeatureExtractor_2SameFeaturesExtractors_exceptionThrown() {
        // Arrange
        List<String> features = new LinkedList<String>();
        features.add("mean_x");
        features.add("mean_x");

        // Act
        FeatureExtractorFactory featureExtractorFactory = getTestFeatureExtractorFactory(features);
    }

    @Test
    public void getFeatureExtractor_1featuresExtractorWithWhiteSpace_1childExtractors() {
        // Arrange
        List<String> features = new LinkedList<String>();
        features.add("mean_x  ");
        FeatureExtractorFactory featureExtractorFactory = getTestFeatureExtractorFactory(features);

        // Act
        FeatureExtractor fe = featureExtractorFactory.getFeatureExtractor();
        int childCount = ((CompositeFeatureExtractor) fe).getChildFeatureExtractors().size();

        // Assert
        assertEquals(1, childCount);
    }

    @Test(expected = UnknownFeatureExtractorTypeException.class)
    public void getFeatureExtractor_1unknownFeaturesExtractor_unknownFeatureExtractorTypeException() {
        // Arrange
        List<String> features = new LinkedList<String>();
        features.add("someUnknownFeature1234");
        FeatureExtractorFactory featureExtractorFactory = getTestFeatureExtractorFactory(features);

        // Act
        FeatureExtractor fe = featureExtractorFactory.getFeatureExtractor();
        int childCount = ((CompositeFeatureExtractor) fe).getChildFeatureExtractors().size();

        // Assert
        assertEquals(1, childCount);
    }

    @Test(expected = InvalidCustomFeatureExtractorNameException.class)
    public void getFeatureExtractor_customFeatureExtractorWithConflictingName_throwException() {
        // Arrange
        LinkedList<String> selectedFeatures = new LinkedList<String>();
        selectedFeatures.add("mean_x");
        FeatureExtractorFactory testFeatureExtractorFactory = getTestFeatureExtractorFactory(selectedFeatures);
        CustomFeatureExtractorLoader customFeatureExtractorLoader = createNiceMock(CustomFeatureExtractorLoader.class);
        LinkedList<FeatureExtractor> featureExtractorList = new LinkedList<FeatureExtractor>();
        FeatureExtractor duplicateFeatureExtractor = new CustomFeatureExtractor("mean_x", "8");
        featureExtractorList.add(duplicateFeatureExtractor);
        expect(customFeatureExtractorLoader.getCustomFeatureExtractors()).andReturn(featureExtractorList).anyTimes();
        replay(customFeatureExtractorLoader);
        testFeatureExtractorFactory.setCustomFeatureExtractorLoader(customFeatureExtractorLoader);

        // Act
        testFeatureExtractorFactory.getFeatureExtractor();

        // Assert
    }

    private FeatureExtractorFactory getTestFeatureExtractorFactory(List<String> features) {
        NoiseXFeatureExtractor noiseXFeatureExtractor = createNiceMock(NoiseXFeatureExtractor.class);
        NoiseYFeatureExtractor noiseYFeatureExtractor = createNiceMock(NoiseYFeatureExtractor.class);
        NoiseZFeatureExtractor noiseZFeatureExtractor = createNiceMock(NoiseZFeatureExtractor.class);
        MeanAbsDerXFeatureExtractor meanAbsDerXFeatureExtractor = createNiceMock(MeanAbsDerXFeatureExtractor.class);
        MeanAbsDerYFeatureExtractor meanAbsDerYFeatureExtractor = createNiceMock(MeanAbsDerYFeatureExtractor.class);
        MeanAbsDerZFeatureExtractor meanAbsDerZFeatureExtractor = createNiceMock(MeanAbsDerZFeatureExtractor.class);
        NoisePerAbsDerXFeatureExtractor noisePerAbsDerXFeatureExtractor = createNiceMock(NoisePerAbsDerXFeatureExtractor.class);
        NoisePerAbsDerYFeatureExtractor noisePerAbsDerYFeatureExtractor = createNiceMock(NoisePerAbsDerYFeatureExtractor.class);
        NoisePerAbsDerZFeatureExtractor noisePerAbsDerZFeatureExtractor = createNiceMock(NoisePerAbsDerZFeatureExtractor.class);
        MeanLocationXFeatureExtractor meanLocationXFeatureExtractor = createNiceMock(MeanLocationXFeatureExtractor.class);
        MeanLocationYFeatureExtractor meanLocationYFeatureExtractor = createNiceMock(MeanLocationYFeatureExtractor.class);
        MeanLocationZFeatureExtractor meanLocationZFeatureExtractor = createNiceMock(MeanLocationZFeatureExtractor.class);
        StdLocationXFeatureExtractor stdLocationXFeatureExtractor = createNiceMock(StdLocationXFeatureExtractor.class);
        StdLocationYFeatureExtractor stdLocationYFeatureExtractor = createNiceMock(StdLocationYFeatureExtractor.class);
        StdLocationZFeatureExtractor stdLocationZFeatureExtractor = createNiceMock(StdLocationZFeatureExtractor.class);
        MeanPitchFeatureExtractor meanPitchFeatureExtractor = createNiceMock(MeanPitchFeatureExtractor.class);
        StdPitchFeatureExtractor stdPitchFeatureExtractor = createNiceMock(StdPitchFeatureExtractor.class);
        MeanRollFeatureExtractor meanRollFeatureExtractor = createNiceMock(MeanRollFeatureExtractor.class);
        StdRollFeatureExtractor stdRollFeatureExtractor = createNiceMock(StdRollFeatureExtractor.class);
        GpsSpeedFeatureExtractor gpsSpeedFeatureExtractor = createNiceMock(GpsSpeedFeatureExtractor.class);
        FundFreqXFeatureExtractor fundFreqXFeatureExtractor = createNiceMock(FundFreqXFeatureExtractor.class);
        FundFreqYFeatureExtractor fundFreqYFeatureExtractor = createNiceMock(FundFreqYFeatureExtractor.class);
        FundFreqZFeatureExtractor fundFreqZFeatureExtractor = createNiceMock(FundFreqZFeatureExtractor.class);
        OdbaFeatureExtractor odbaFeatureExtractor = createNiceMock(OdbaFeatureExtractor.class);
        VedbaFeatureExtractor vedbaFeatureExtractor = createNiceMock(VedbaFeatureExtractor.class);
        RawInputFeatureExtractor rawInputFeatureExtractor = createNiceMock(RawInputFeatureExtractor.class);
        FirstXFeatureExtractor firstXFeatureExtractor = createNiceMock(FirstXFeatureExtractor.class);
        FirstYFeatureExtractor firstYFeatureExtractor = createNiceMock(FirstYFeatureExtractor.class);
        FirstZFeatureExtractor firstZFeatureExtractor = createNiceMock(FirstZFeatureExtractor.class);
        MeasurementClassifierFeatureExtractor measurementClassifierFeatureExtractor = createNiceMock(MeasurementClassifierFeatureExtractor.class);
        CorrelationXYFeatureExtractor correlationXYFeatureExtractor = createNiceMock(CorrelationXYFeatureExtractor.class);
        CorrelationYZFeatureExtractor correlationYZFeatureExtractor = createNiceMock(CorrelationYZFeatureExtractor.class);
        CorrelationXZFeatureExtractor correlationXZFeatureExtractor = createNiceMock(CorrelationXZFeatureExtractor.class);
        FundFreqCorrelationXFeatureExtractor fundFreqCorrelationXFeatureExtractor = createNiceMock(FundFreqCorrelationXFeatureExtractor.class);
        FundFreqCorrelationYFeatureExtractor fundFreqCorrelationYFeatureExtractor = createNiceMock(FundFreqCorrelationYFeatureExtractor.class);
        FundFreqCorrelationZFeatureExtractor fundFreqCorrelationZFeatureExtractor = createNiceMock(FundFreqCorrelationZFeatureExtractor.class);
        FundFreqMagnitudeXFeatureExtractor fundFreqMagnitudeXFeatureExtractor = createNiceMock(FundFreqMagnitudeXFeatureExtractor.class);
        FundFreqMagnitudeYFeatureExtractor fundFreqMagnitudeYFeatureExtractor = createNiceMock(FundFreqMagnitudeYFeatureExtractor.class);
        FundFreqMagnitudeZFeatureExtractor fundFreqMagnitudeZFeatureExtractor = createNiceMock(FundFreqMagnitudeZFeatureExtractor.class);
        StepResponseFeatureExtractor stepResponseFeatureExtractor = createNiceMock(StepResponseFeatureExtractor.class);
        AltitudeFeatureExtractor altitudeFeatureExtractor = createNiceMock(AltitudeFeatureExtractor.class);

        expect(meanLocationXFeatureExtractor.getName()).andReturn("mean_x").anyTimes();
        replay(meanLocationXFeatureExtractor);
        expect(meanLocationYFeatureExtractor.getName()).andReturn("mean_y").anyTimes();
        replay(meanLocationYFeatureExtractor);

        FeatureExtractorFactory featureExtractorFactory = new FeatureExtractorFactory(StringUtils.join(features, ", "),
                meanLocationXFeatureExtractor, meanLocationYFeatureExtractor, meanLocationZFeatureExtractor,
                stdLocationXFeatureExtractor, stdLocationYFeatureExtractor, stdLocationZFeatureExtractor,
                meanPitchFeatureExtractor, stdPitchFeatureExtractor, meanRollFeatureExtractor, stdRollFeatureExtractor,
                gpsSpeedFeatureExtractor, meanAbsDerXFeatureExtractor, meanAbsDerYFeatureExtractor, meanAbsDerZFeatureExtractor,
                noiseXFeatureExtractor, noiseYFeatureExtractor, noiseZFeatureExtractor, noisePerAbsDerXFeatureExtractor,
                noisePerAbsDerYFeatureExtractor, noisePerAbsDerZFeatureExtractor, fundFreqXFeatureExtractor,
                fundFreqYFeatureExtractor, fundFreqZFeatureExtractor, odbaFeatureExtractor, vedbaFeatureExtractor,
                rawInputFeatureExtractor, firstXFeatureExtractor, firstYFeatureExtractor, firstZFeatureExtractor,
                measurementClassifierFeatureExtractor, correlationXYFeatureExtractor, correlationXZFeatureExtractor,
                correlationYZFeatureExtractor, fundFreqCorrelationXFeatureExtractor, fundFreqCorrelationYFeatureExtractor,
                fundFreqCorrelationZFeatureExtractor, fundFreqMagnitudeXFeatureExtractor, fundFreqMagnitudeYFeatureExtractor,
                fundFreqMagnitudeZFeatureExtractor, stepResponseFeatureExtractor, altitudeFeatureExtractor);

        CustomFeatureExtractorLoader customFeatureExtractorLoader = createNiceMock(CustomFeatureExtractorLoader.class);
        expect(customFeatureExtractorLoader.getCustomFeatureExtractors()).andReturn(new LinkedList<FeatureExtractor>())
                .anyTimes();
        replay(customFeatureExtractorLoader);
        featureExtractorFactory.setCustomFeatureExtractorLoader(customFeatureExtractorLoader);
        return featureExtractorFactory;
    }
}
