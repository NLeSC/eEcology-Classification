package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.machinelearning.GeneralTrainer;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.Classifier;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;

public class GeneralTrainerTest extends TrainerTest {
    @Test
    public void train_zeroInstancesJ48_resultIsJ48Classifier() {
        trainer = createGeneralTrainer("weka.classifiers.trees.J48");
        Instances instances = getTestInstances();

        // Act
        Classifier classifier = trainer.train(instances);

        // Assert
        assertTrue(classifier instanceof J48);
    }

    @Test
    public void train_zeroInstancesRf_resultIsRandomForestClassifier() {
        trainer = createGeneralTrainer("weka.classifiers.trees.RandomForest");
        Instances instances = getTestInstances();

        // Act
        Classifier classifier = trainer.train(instances);

        // Assert
        assertTrue(classifier instanceof RandomForest);
    }

    @Test
    public void train_setReducedErrorPruning_j48HasCorrectSetting() {
        // Arrange

        // Act
        GeneralTrainer j48Trainer = createGeneralTrainer("weka.classifiers.trees.J48 -R"); // -R for reduced error pruning
        J48 j48 = (J48) j48Trainer.train(getTestInstances());

        // Assert
        assertTrue(j48.getReducedErrorPruning());
    }

    @Test
    public void train_setForestSize_randomForestHasCorrectSetting() {
        // Arrange

        // Act
        GeneralTrainer randomForestTrainer = createGeneralTrainer("weka.classifiers.trees.RandomForest -I 800"); // -I for number or trees
        RandomForest randomForest = (RandomForest) randomForestTrainer.train(getTestInstances());

        // Assert
        assertEquals(800, randomForest.getNumTrees());
    }

    @Test
    public void train_attributeSelectedJ48_innerClassifierIsJ48() {
        // Arrange
        String classifierString = "weka.classifiers.meta.AttributeSelectedClassifier -E \"weka.attributeSelection.CfsSubsetEval\" -S \"weka.attributeSelection.BestFirst -D 1 -N 5\" -W weka.classifiers.trees.J48 -- -C 0.25 -M 2";

        // Act
        GeneralTrainer attributeSelectedTrainer = createGeneralTrainer(classifierString);
        AttributeSelectedClassifier attributeSelectedClassifier = (AttributeSelectedClassifier) attributeSelectedTrainer
                .train(getTestInstances());

        // Assert
        assertTrue("Classifier within wrapper should be J48", attributeSelectedClassifier.getClassifier() instanceof J48);
    }

    private GeneralTrainer createGeneralTrainer(String classifierOptions) {
        return new GeneralTrainer(classifierOptions);
    }

    @Before
    public void setup() {
        trainer = createGeneralTrainer("weka.classifiers.trees.J48");
    }
}