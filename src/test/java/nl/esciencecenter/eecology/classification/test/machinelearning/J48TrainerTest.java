package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.machinelearning.J48Trainer;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class J48TrainerTest extends TrainerTest {
    @Test
    public void train_zeroInstances_resultIsJ48Classifier() {
        trainer = createJ48Trainer();
        Instances instances = getTestInstances();

        // Act                  
        Classifier classifier = trainer.train(instances);

        // Assert
        assertTrue(classifier instanceof J48);
    }

    public void train_setReducedErrorPruning_j48HasCorrectSetting() {
        // Arrange
        boolean reducedErrorPruning = true;

        // Act
        J48Trainer j48Trainer = new J48Trainer(reducedErrorPruning);
        J48 j48 = (J48) j48Trainer.train(getTestInstances());

        // Assert
        assertTrue(j48.getReducedErrorPruning());
    }

    private J48Trainer createJ48Trainer() {
        return new J48Trainer(false);
    }

    @Before
    public void setup() {
        trainer = createJ48Trainer();
    }
}