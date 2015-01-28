package nl.esciencecenter.eecology.classification.dataaccess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import nl.esciencecenter.eecology.classification.featureextraction.FeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.CustomFeatureExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.MeanExtractor;
import nl.esciencecenter.eecology.classification.featureextraction.featureextractors.StdExtractor;

import com.google.inject.Inject;

public class CustomFeatureExtractorLoader {
    @Inject
    private PathManager pathManager;
    @Inject
    private MeanExtractor meanExtractor;
    @Inject
    private StdExtractor stdExtractor;

    public void setPathmanager(PathManager pathmanager) {
        pathManager = pathmanager;
    }

    public List<FeatureExtractor> getCustomFeatureExtractors() {
        String sourceLocation = pathManager.getCustomFeaturePath();
        if (sourceLocation == null || sourceLocation.equalsIgnoreCase("")) {
            return new LinkedList<FeatureExtractor>();
        }
        return getCustomFeatureExtractorsFromFile();
    }

    private LinkedList<FeatureExtractor> getCustomFeatureExtractorsFromFile() {
        BufferedReader reader = getReader();
        LinkedList<FeatureExtractor> results = new LinkedList<FeatureExtractor>();
        String line = readLine(reader);
        while (line != null) {
            CustomFeatureExtractor customFeatureExtractor = getCustomFeatureExtractorFromLine(line);
            results.add(customFeatureExtractor);
            line = readLine(reader);
        }
        return results;
    }

    private CustomFeatureExtractor getCustomFeatureExtractorFromLine(String line) {
        String[] elements = line.split(";");
        String name = elements[0].trim();
        String expression = elements[1].trim();
        CustomFeatureExtractor customFeatureExtractor = new CustomFeatureExtractor(name, expression);
        customFeatureExtractor.setMeanExtractor(meanExtractor);
        customFeatureExtractor.setStdExtractor(stdExtractor);
        return customFeatureExtractor;
    }

    private BufferedReader getReader() {
        FileReader fileReader = null;
        String customFeaturePath = pathManager.getCustomFeaturePath();
        try {
            fileReader = new FileReader(customFeaturePath);
        } catch (FileNotFoundException e) {
            String message = "The file containing custom feature definitions was not found at (" + customFeaturePath + ").";
            throw new RuntimeException(message, e);
        }

        return new BufferedReader(fileReader);
    }

    private String readLine(BufferedReader reader) {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
        }
        return line;
    }
}
