package nl.esciencecenter.eecology.classification.dataaccess;

import nl.esciencecenter.eecology.classification.configuration.PathManager;
import weka.classifiers.Classifier;

import com.google.inject.Inject;

public class ClassifierLoader {
    @Inject
    private PathManager pathManager;

    public Classifier loadClassifier() {
        String inputClassifierPath = pathManager.getClassifierPath();
        return loadClassifier(inputClassifierPath);
    }

    public Classifier loadClassifier(String inputClassifierPath) {
        Classifier classifier;
        try {
            classifier = (Classifier) weka.core.SerializationHelper.read(inputClassifierPath);
        } catch (Exception e) {
            throw new RuntimeException("Error loading classifier from \"" + inputClassifierPath + "\".", e);
        }
        return classifier;
    }
}
