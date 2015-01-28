package nl.esciencecenter.eecology.classification.machinelearning;

import nl.esciencecenter.eecology.classification.machinelearning.exceptions.ClassifierBuildingException;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RandomForestTrainer implements Trainer {

    private final int numTrees;

    @Inject
    public RandomForestTrainer(@Named("random_forest_number_of_trees") int numTrees) {
        this.numTrees = numTrees;
    }

    @Override
    public RandomForest train(Instances instances) {
        RandomForest randomForest = new RandomForest();
        randomForest.setNumTrees(numTrees);
        try {
            randomForest.buildClassifier(instances);
        } catch (Exception e) {
            throw new ClassifierBuildingException("Exception occured while building classifier: " + e.getMessage(), e);
        }
        return randomForest;
    }

}
