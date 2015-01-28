package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.machinelearning.J48Trainer;
import nl.esciencecenter.eecology.classification.machinelearning.RandomForestTrainer;
import nl.esciencecenter.eecology.classification.machinelearning.Trainer;
import nl.esciencecenter.eecology.classification.machinelearning.TrainerFactory;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.UnknownMachineLearningAlgorithmException;

import org.junit.Test;

public class TrainerFactoryTest {
    @Test
    public void test_canBeCreated() {
        createNewTrainerFactoryInstance(TrainerFactory.TYPE_RANDOM_FOREST);
    }

    @Test
    public void train_returnRfClassifier() {
        // Arrange
        TrainerFactory trainerFactory = createNewTrainerFactoryInstance(TrainerFactory.TYPE_RANDOM_FOREST);

        // Act                  
        Trainer trainer = trainerFactory.getTrainer();

        // Assert
        assertTrue(trainer instanceof RandomForestTrainer);
    }

    @Test
    public void j48Train_returnJ48Classifier() {
        // Arrange
        TrainerFactory trainerFactory = createNewTrainerFactoryInstance(TrainerFactory.TYPE_J48);

        // Act                  
        Trainer trainer = trainerFactory.getTrainer();

        // Assert
        assertTrue(trainer instanceof J48Trainer);
    }

    @Test(expected = UnknownMachineLearningAlgorithmException.class)
    public void unknownType_unknownMachineLearningAlgorithmExceptionIsThrown() {
        // Arrange

        // Act
        createNewTrainerFactoryInstance("someUnknownType1234");

        // Assert
    }

    private TrainerFactory createNewTrainerFactoryInstance(String type) {
        return new TrainerFactory(type, createNiceMock(RandomForestTrainer.class), createNiceMock(J48Trainer.class));
    }
}
