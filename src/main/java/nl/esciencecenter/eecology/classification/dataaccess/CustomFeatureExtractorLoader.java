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
        return getCustomFeatureExtractorsFromFile(sourceLocation);
    }

    private LinkedList<FeatureExtractor> getCustomFeatureExtractorsFromFile(String path) {
        BufferedReader reader = getReader(path);
        LinkedList<FeatureExtractor> results = new LinkedList<FeatureExtractor>();
        String line = readLine(reader, path);
        while (line != null) {
            CustomFeatureExtractor customFeatureExtractor = getCustomFeatureExtractorFromLine(line, path);
            results.add(customFeatureExtractor);
            line = readLine(reader, path);
        }
        return results;
    }

    private CustomFeatureExtractor getCustomFeatureExtractorFromLine(String line, String path) {
        String[] elements = line.split(";");
        throwExceptionIfMalformed(line, elements, path);
        String name = elements[0].trim();
        String expression = elements[1].trim();
        CustomFeatureExtractor customFeatureExtractor = new CustomFeatureExtractor(name, expression);
        customFeatureExtractor.setMeanExtractor(meanExtractor);
        customFeatureExtractor.setStdExtractor(stdExtractor);
        return customFeatureExtractor;
    }

    private BufferedReader getReader(String path) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
        } catch (FileNotFoundException e) {
            String message = "The file containing custom feature definitions was not found at (" + path + ").";
            throw new CustomFeatureFileNotFoundException(message, e);
        }

        return new BufferedReader(fileReader);
    }

    private String readLine(BufferedReader reader, String path) {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            String message = "Could not read from the file containing custom feature definitions at (" + path + ").";
            throw new CustomFeatureFileNotFoundException(message, e);
        }
        return line;
    }

    private void throwExceptionIfMalformed(String line, String[] elements, String path) {
        if (elements.length != 2) {
            StringBuilder message = new StringBuilder();
            message.append("Could not load custom features from file at '");
            message.append(path);
            message.append("' because it does not have the correct format: Line '");
            message.append(line);
            message.append("' was not formatted as 'name; equation'");
            throw new CustomFeatureFileMalformedException(message.toString(), null);
        }
    }
}
