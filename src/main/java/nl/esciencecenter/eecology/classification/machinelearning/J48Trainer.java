package nl.esciencecenter.eecology.classification.machinelearning;

import javax.inject.Named;

import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierBuildingException;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

import com.google.inject.Inject;

public class J48Trainer implements Trainer {

    private final boolean reducedErrorPruning;

    @Inject
    public J48Trainer(@Named("j48_reduced_error_pruning") boolean reducedErrorPruning) {
        this.reducedErrorPruning = reducedErrorPruning;
    }

    @Override
    public Classifier train(Instances instances) {
        J48 j48 = new J48();
        j48.setReducedErrorPruning(reducedErrorPruning);
        try {
            j48.buildClassifier(instances);
        } catch (Exception e) {
            throw new ClassifierBuildingException("Exception occured while building classifier: " + e.getMessage(), e);
        }
        return j48;
    }

}
