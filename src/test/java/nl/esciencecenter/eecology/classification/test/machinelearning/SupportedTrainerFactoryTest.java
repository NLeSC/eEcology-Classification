package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.machinelearning.J48Trainer;
import nl.esciencecenter.eecology.classification.machinelearning.RandomForestTrainer;
import nl.esciencecenter.eecology.classification.machinelearning.SupportedTrainerFactory;
import nl.esciencecenter.eecology.classification.machinelearning.Trainer;
import nl.esciencecenter.eecology.classification.machinelearning.exceptions.UnknownMachineLearningAlgorithmException;

import org.junit.Test;

public class SupportedTrainerFactoryTest {
    @Test
    public void test_canBeCreated() {
        createNewTrainerFactoryInstance(SupportedTrainerFactory.TYPE_RANDOM_FOREST);
    }

    @Test
    public void train_returnRfClassifier() {
        // Arrange
        SupportedTrainerFactory trainerFactory = createNewTrainerFactoryInstance(SupportedTrainerFactory.TYPE_RANDOM_FOREST);

        // Act
        Trainer trainer = trainerFactory.getTrainer();

        // Assert
        assertTrue(trainer instanceof RandomForestTrainer);
    }

    @Test
    public void j48Train_returnJ48Classifier() {
        // Arrange
        SupportedTrainerFactory trainerFactory = createNewTrainerFactoryInstance(SupportedTrainerFactory.TYPE_J48);

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

    private SupportedTrainerFactory createNewTrainerFactoryInstance(String type) {
        return new SupportedTrainerFactory(type, createNiceMock(RandomForestTrainer.class), createNiceMock(J48Trainer.class));
    }
}
