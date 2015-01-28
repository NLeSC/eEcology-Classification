package nl.esciencecenter.eecology.classification.featureextraction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nl.esciencecenter.eecology.classification.dataaccess.CustomFeatureExtractorLoader;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.AltitudeFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CompositeFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CorrelationXYFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CorrelationXZFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CorrelationYZFeatureExtractor;
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

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Creates a feature extractor object composed of various feature extractors based on a comma separated list given to the
 * constructor.
 *
 * @author Christiaan Meijer, Elena Ranguelova NLeSc
 *
 */
public class FeatureExtractorFactory {
    @Inject
    CustomFeatureExtractorLoader customFeatureExtractorLoader;

    private final List<String> featureNames;
    private final NoiseXFeatureExtractor noiseXFeatureExtractor;
    private final NoiseYFeatureExtractor noiseYFeatureExtractor;
    private final NoiseZFeatureExtractor noiseZFeatureExtractor;
    private final NoisePerAbsDerXFeatureExtractor noisePerAbsDerXFeatureExtractor;
    private final NoisePerAbsDerYFeatureExtractor noisePerAbsDerYFeatureExtractor;
    private final NoisePerAbsDerZFeatureExtractor noisePerAbsDerZFeatureExtractor;
    private final MeanAbsDerXFeatureExtractor meanAbsDerXFeatureExtractor;
    private final MeanAbsDerYFeatureExtractor meanAbsDerYFeatureExtractor;
    private final MeanAbsDerZFeatureExtractor meanAbsDerZFeatureExtractor;
    private final MeanLocationXFeatureExtractor meanLocationXFeatureExtractor;
    private final MeanLocationYFeatureExtractor meanLocationYFeatureExtractor;
    private final MeanLocationZFeatureExtractor meanLocationZFeatureExtractor;
    private final StdLocationXFeatureExtractor stdLocationXFeatureExtractor;
    private final StdLocationYFeatureExtractor stdLocationYFeatureExtractor;
    private final StdLocationZFeatureExtractor stdLocationZFeatureExtractor;
    private final MeanPitchFeatureExtractor meanPitchFeatureExtractor;
    private final StdPitchFeatureExtractor stdPitchFeatureExtractor;
    private final MeanRollFeatureExtractor meanRollFeatureExtractor;
    private final StdRollFeatureExtractor stdRollFeatureExtractor;
    private final CorrelationXYFeatureExtractor correlationXYFeatureExtractor;
    private final CorrelationXZFeatureExtractor correlationXZFeatureExtractor;
    private final CorrelationYZFeatureExtractor correlationYZFeatureExtractor;
    private final GpsSpeedFeatureExtractor gpsSpeedFeatureExtractor;
    private final FundFreqXFeatureExtractor fundFreqXFeatureExtractor;
    private final FundFreqYFeatureExtractor fundFreqYFeatureExtractor;
    private final FundFreqZFeatureExtractor fundFreqZFeatureExtractor;
    private final OdbaFeatureExtractor odbaFeatureExtractor;
    private final VedbaFeatureExtractor vedbaFeatureExtractor;
    private final RawInputFeatureExtractor rawInputFeatureExtractor;
    private final FirstXFeatureExtractor firstXFeatureExtractor;
    private final FirstYFeatureExtractor firstYFeatureExtractor;
    private final FirstZFeatureExtractor firstZFeatureExtractor;
    private final MeasurementClassifierFeatureExtractor measurementClassifierFeatureExtractor;
    private final FundFreqCorrelationXFeatureExtractor fundFreqCorrelationXFeatureExtractor;
    private final FundFreqCorrelationYFeatureExtractor fundFreqCorrelationYFeatureExtractor;
    private final FundFreqCorrelationZFeatureExtractor fundFreqCorrelationZFeatureExtractor;
    private final FundFreqMagnitudeXFeatureExtractor fundFreqMagnitudeXFeatureExtractor;
    private final FundFreqMagnitudeYFeatureExtractor fundFreqMagnitudeYFeatureExtractor;
    private final FundFreqMagnitudeZFeatureExtractor fundFreqMagnitudeZFeatureExtractor;
    private final AltitudeFeatureExtractor altitudeFeatureExtractor;
    private final StepResponseFeatureExtractor stepResponseFeatureExtractor;

    @Inject
    public FeatureExtractorFactory(@Named("extract_features") String features,
            MeanLocationXFeatureExtractor meanLocationXFeatureExtractor,
            MeanLocationYFeatureExtractor meanLocationYFeatureExtractor,
            MeanLocationZFeatureExtractor meanLocationZFeatureExtractor,
            StdLocationXFeatureExtractor stdLocationXFeatureExtractor, StdLocationYFeatureExtractor stdLocationYFeatureExtractor,
            StdLocationZFeatureExtractor stdLocationZFeatureExtractor, MeanPitchFeatureExtractor meanPitchFeatureExtractor,
            StdPitchFeatureExtractor stdPitchFeatureExtractor, MeanRollFeatureExtractor meanRollFeatureExtractor,
            StdRollFeatureExtractor stdRollFeatureExtractor, GpsSpeedFeatureExtractor gpsSpeedFeatureExtractor,
            MeanAbsDerXFeatureExtractor meanAbsDerXFeatureExtractor, MeanAbsDerYFeatureExtractor meanAbsDerYFeatureExtractor,
            MeanAbsDerZFeatureExtractor meanAbsDerZFeatureExtractor, NoiseXFeatureExtractor noiseXFeatureExtractor,
            NoiseYFeatureExtractor noiseYFeatureExtractor, NoiseZFeatureExtractor noiseZFeatureExtractor,
            NoisePerAbsDerXFeatureExtractor noisePerAbsDerXFeatureExtractor,
            NoisePerAbsDerYFeatureExtractor noisePerAbsDerYFeatureExtractor,
            NoisePerAbsDerZFeatureExtractor noisePerAbsDerZFeatureExtractor, FundFreqXFeatureExtractor fundFreqXFeatureExtractor,
            FundFreqYFeatureExtractor fundFreqYFeatureExtractor, FundFreqZFeatureExtractor fundFreqZFeatureExtractor,
            OdbaFeatureExtractor odbaFeatureExtractor, VedbaFeatureExtractor vedbaFeatureExtractor,
            RawInputFeatureExtractor rawInputFeatureExtractor, FirstXFeatureExtractor firstXFeatureExtractor,
            FirstYFeatureExtractor firstYFeatureExtractor, FirstZFeatureExtractor firstZFeatureExtractor,
            MeasurementClassifierFeatureExtractor measurementClassifierFeatureExtractor,
            CorrelationXYFeatureExtractor correlationXYFeatureExtractor,
            CorrelationXZFeatureExtractor correlationXZFeatureExtractor,
            CorrelationYZFeatureExtractor correlationYZFeatureExtractor,
            FundFreqCorrelationXFeatureExtractor fundFreqCorrelationXFeatureExtractor,
            FundFreqCorrelationYFeatureExtractor fundFreqCorrelationYFeatureExtractor,
            FundFreqCorrelationZFeatureExtractor fundFreqCorrelationZFeatureExtractor,
            FundFreqMagnitudeXFeatureExtractor fundFreqMagnitudeXFeatureExtractor,
            FundFreqMagnitudeYFeatureExtractor fundFreqMagnitudeYFeatureExtractor,
            FundFreqMagnitudeZFeatureExtractor fundFreqMagnitudeZFeatureExtractor,
            StepResponseFeatureExtractor stepResponseFeatureExtractor, AltitudeFeatureExtractor altitudeFeatureExtractor) {
        featureNames = getFeatureNames(getTrimmedList(getList(features)));
        this.meanAbsDerXFeatureExtractor = meanAbsDerXFeatureExtractor;
        this.meanAbsDerYFeatureExtractor = meanAbsDerYFeatureExtractor;
        this.meanAbsDerZFeatureExtractor = meanAbsDerZFeatureExtractor;
        this.meanLocationXFeatureExtractor = meanLocationXFeatureExtractor;
        this.meanLocationYFeatureExtractor = meanLocationYFeatureExtractor;
        this.meanLocationZFeatureExtractor = meanLocationZFeatureExtractor;
        this.stdLocationXFeatureExtractor = stdLocationXFeatureExtractor;
        this.stdLocationYFeatureExtractor = stdLocationYFeatureExtractor;
        this.stdLocationZFeatureExtractor = stdLocationZFeatureExtractor;
        this.meanPitchFeatureExtractor = meanPitchFeatureExtractor;
        this.stdPitchFeatureExtractor = stdPitchFeatureExtractor;
        this.meanRollFeatureExtractor = meanRollFeatureExtractor;
        this.stdRollFeatureExtractor = stdRollFeatureExtractor;
        this.gpsSpeedFeatureExtractor = gpsSpeedFeatureExtractor;
        this.noiseXFeatureExtractor = noiseXFeatureExtractor;
        this.noiseYFeatureExtractor = noiseYFeatureExtractor;
        this.noiseZFeatureExtractor = noiseZFeatureExtractor;
        this.noisePerAbsDerXFeatureExtractor = noisePerAbsDerXFeatureExtractor;
        this.noisePerAbsDerYFeatureExtractor = noisePerAbsDerYFeatureExtractor;
        this.noisePerAbsDerZFeatureExtractor = noisePerAbsDerZFeatureExtractor;
        this.fundFreqXFeatureExtractor = fundFreqXFeatureExtractor;
        this.fundFreqYFeatureExtractor = fundFreqYFeatureExtractor;
        this.fundFreqZFeatureExtractor = fundFreqZFeatureExtractor;
        this.odbaFeatureExtractor = odbaFeatureExtractor;
        this.vedbaFeatureExtractor = vedbaFeatureExtractor;
        this.rawInputFeatureExtractor = rawInputFeatureExtractor;
        this.firstXFeatureExtractor = firstXFeatureExtractor;
        this.firstYFeatureExtractor = firstYFeatureExtractor;
        this.firstZFeatureExtractor = firstZFeatureExtractor;
        this.measurementClassifierFeatureExtractor = measurementClassifierFeatureExtractor;
        this.correlationXYFeatureExtractor = correlationXYFeatureExtractor;
        this.correlationXZFeatureExtractor = correlationXZFeatureExtractor;
        this.correlationYZFeatureExtractor = correlationYZFeatureExtractor;
        this.fundFreqCorrelationXFeatureExtractor = fundFreqCorrelationXFeatureExtractor;
        this.fundFreqCorrelationYFeatureExtractor = fundFreqCorrelationYFeatureExtractor;
        this.fundFreqCorrelationZFeatureExtractor = fundFreqCorrelationZFeatureExtractor;
        this.fundFreqMagnitudeXFeatureExtractor = fundFreqMagnitudeXFeatureExtractor;
        this.fundFreqMagnitudeYFeatureExtractor = fundFreqMagnitudeYFeatureExtractor;
        this.fundFreqMagnitudeZFeatureExtractor = fundFreqMagnitudeZFeatureExtractor;
        this.altitudeFeatureExtractor = altitudeFeatureExtractor;
        this.stepResponseFeatureExtractor = stepResponseFeatureExtractor;
    }

    public void setCustomFeatureExtractorLoader(CustomFeatureExtractorLoader customFeatureExtractorLoader) {
        this.customFeatureExtractorLoader = customFeatureExtractorLoader;
    }

    public FeatureExtractor getFeatureExtractor() {
        CompositeFeatureExtractor compositeFeatureExtractor = new CompositeFeatureExtractor();

        if (featureNames != null) {
            for (String featureName : featureNames) {
                HashMap<String, FeatureExtractor> featureMap = getPossibleFeaturesByName();
                if (featureMap.containsKey(featureName)) {
                    compositeFeatureExtractor.addChild(featureMap.get(featureName));
                } else {
                    throw new UnknownFeatureExtractorTypeException("Unknown feature type: " + featureName);
                }
            }
        }
        return compositeFeatureExtractor;
    }

    private List<String> getFeatureNames(List<String> featuresList) {
        Set<String> featureSet = new HashSet<String>();
        if (featuresList != null) {
            featureSet.addAll(featuresList);
            if (featureSet.size() != featuresList.size()) {
                throw new IllegalArgumentException(
                        "The list of selected features contains duplicates. Each feature is only allowed once.");
            }
        }
        return featuresList;
    }

    private static List<String> getList(String features) {
        LinkedList<String> trimmedFeatures = new LinkedList<String>();

        if (features == null) {
            return trimmedFeatures;
        }

        List<String> featureList = Arrays.asList(features.split(","));
        for (String feature : featureList) {
            trimmedFeatures.add(feature);
        }
        return trimmedFeatures;
    }

    private List<String> getTrimmedList(List<String> features) {
        List<String> trimmedFeatures = new LinkedList<String>();
        for (int i = 0; i < features.size(); i++) {
            String feature = features.get(i).trim();
            if (feature.equalsIgnoreCase("")) {
                continue;
            }
            trimmedFeatures.add(i, feature);
        }
        return trimmedFeatures;
    }

    private HashMap<String, FeatureExtractor> getPossibleFeaturesByName() {
        HashMap<String, FeatureExtractor> featureMap = new HashMap<String, FeatureExtractor>();
        for (FeatureExtractor extractor : getStandardFeatureExtractorList()) {
            featureMap.put(extractor.getName(), extractor);
        }

        for (FeatureExtractor customFeatureExtractor : customFeatureExtractorLoader.getCustomFeatureExtractors()) {
            String customFeatureExtractorName = customFeatureExtractor.getName();
            if (featureMap.containsKey(customFeatureExtractorName)) {
                String message = "The custom feature extractor name '" + customFeatureExtractorName + "' is not unique.";
                throw new InvalidCustomFeatureExtractorNameException(message);
            } else {
                featureMap.put(customFeatureExtractorName, customFeatureExtractor);
            }
        }
        return featureMap;
    }

    private List<FeatureExtractor> getStandardFeatureExtractorList() {
        LinkedList<FeatureExtractor> featureExtractorList = new LinkedList<FeatureExtractor>(
                Arrays.asList(new FeatureExtractor[] { meanLocationXFeatureExtractor, meanLocationYFeatureExtractor,
                        meanLocationZFeatureExtractor, stdLocationXFeatureExtractor, stdLocationYFeatureExtractor,
                        stdLocationZFeatureExtractor, meanPitchFeatureExtractor, stdPitchFeatureExtractor,
                        meanRollFeatureExtractor, stdRollFeatureExtractor, gpsSpeedFeatureExtractor, meanAbsDerXFeatureExtractor,
                        meanAbsDerYFeatureExtractor, meanAbsDerZFeatureExtractor, noiseXFeatureExtractor, noiseYFeatureExtractor,
                        noiseZFeatureExtractor, noisePerAbsDerXFeatureExtractor, noisePerAbsDerYFeatureExtractor,
                        noisePerAbsDerZFeatureExtractor, fundFreqXFeatureExtractor, fundFreqYFeatureExtractor,
                        fundFreqZFeatureExtractor, odbaFeatureExtractor, vedbaFeatureExtractor, rawInputFeatureExtractor,
                        firstXFeatureExtractor, firstYFeatureExtractor, firstZFeatureExtractor,
                        measurementClassifierFeatureExtractor, correlationXYFeatureExtractor, correlationYZFeatureExtractor,
                        correlationXZFeatureExtractor, fundFreqCorrelationXFeatureExtractor,
                        fundFreqCorrelationYFeatureExtractor, fundFreqCorrelationZFeatureExtractor,
                        fundFreqMagnitudeXFeatureExtractor, fundFreqMagnitudeYFeatureExtractor,
                        fundFreqMagnitudeZFeatureExtractor, stepResponseFeatureExtractor, altitudeFeatureExtractor }));
        return featureExtractorList;
    }
}
