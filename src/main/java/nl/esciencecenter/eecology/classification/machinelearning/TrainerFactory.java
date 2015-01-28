package nl.esciencecenter.eecology.classification.machinelearning;

import java.util.HashMap;
import java.util.Map;

import nl.esciencecenter.eecology.classification.machinelearning.exceptions.UnknownMachineLearningAlgorithmException;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class TrainerFactory {

    public static final String TYPE_RANDOM_FOREST = "random_forest";
    /**
     * Java implementation of C4.5 tree learning algorithm.
     */
    public static final String TYPE_J48 = "j48";
    private final RandomForestTrainer randomForestTrainer;
    private final J48Trainer j48Trainer;
    private final String machineLearningAlgorithm;
    private Map<String, Trainer> trainersByName;

    @Inject
    public TrainerFactory(@Named("machine_learning_algorithm") String machineLearningAlgorithm,
            RandomForestTrainer randomForestTrainer, J48Trainer j48Trainer) {
        this.machineLearningAlgorithm = machineLearningAlgorithm.trim();
        this.randomForestTrainer = randomForestTrainer;
        this.j48Trainer = j48Trainer;
        trainersByName = initializeTrainersByName();
        checkMachineLearningAlgorithm();
    }

    public Trainer getTrainer() {
        return getTrainersByName().get(machineLearningAlgorithm);
    }

    private Map<String, Trainer> initializeTrainersByName() {
        HashMap<String, Trainer> trainerMap = new HashMap<String, Trainer>();
        trainerMap.put(TYPE_RANDOM_FOREST, randomForestTrainer);
        trainerMap.put(TYPE_J48, j48Trainer);
        return trainerMap;
    }

    private Map<String, Trainer> getTrainersByName() {
        if (trainersByName == null) {
            trainersByName = initializeTrainersByName();
        }
        return trainersByName;
    }

    private void checkMachineLearningAlgorithm() {
        if (getTrainersByName().containsKey(machineLearningAlgorithm) == false) {
            throw new UnknownMachineLearningAlgorithmException("Unknown machine learning algorithm type selected: "
                    + machineLearningAlgorithm);
        }
    }
}
