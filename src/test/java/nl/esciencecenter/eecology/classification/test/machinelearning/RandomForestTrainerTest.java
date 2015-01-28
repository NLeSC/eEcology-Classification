package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.machinelearning.RandomForestTrainer;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.Classifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class RandomForestTrainerTest extends TrainerTest {
    @Test
    public void train_zeroInstances_resultIsRfClassifier() {
        trainer = createRandomForestTrainer();
        Instances instances = getTestInstances();

        // Act                  
        Classifier classifier = trainer.train(instances);

        // Assert
        assertTrue(classifier instanceof RandomForest);
    }

    @Test
    public void constructor_numTreeGiven_rfHasCorrectNumTree() {
        // Arrange

        // Act
        RandomForestTrainer rfTrainer = new RandomForestTrainer(123);
        RandomForest rf = rfTrainer.train(getTestInstances());

        // Assert
        assertEquals(123, rf.getNumTrees());
    }

    private RandomForestTrainer createRandomForestTrainer() {
        return new RandomForestTrainer(100);
    }

    @Before
    public void setup() {
        trainer = createRandomForestTrainer();
    }
}