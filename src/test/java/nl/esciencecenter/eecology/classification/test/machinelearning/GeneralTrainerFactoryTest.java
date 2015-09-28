package nl.esciencecenter.eecology.classification.test.machinelearning;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.assertTrue;
import nl.esciencecenter.eecology.classification.machinelearning.GeneralTrainer;
import nl.esciencecenter.eecology.classification.machinelearning.GeneralTrainerFactory;
import nl.esciencecenter.eecology.classification.machinelearning.Trainer;

import org.junit.Test;

public class GeneralTrainerFactoryTest {
    @Test
    public void test() {
        // Arrange
        GeneralTrainerFactory trainerFactory = new GeneralTrainerFactory();
        trainerFactory.setTrainer(createNiceMock(GeneralTrainer.class));

        // Act
        Trainer trainer = trainerFactory.getTrainer();

        // Assert
        assertTrue(trainer instanceof GeneralTrainer);
    }
}
